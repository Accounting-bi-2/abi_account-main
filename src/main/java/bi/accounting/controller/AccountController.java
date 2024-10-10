package bi.accounting.controller;

import bi.accounting.client.OpenIdClient;
import bi.accounting.client.XeroAPI;
import bi.accounting.client.XeroClient;
import bi.accounting.dto.AccountDTO;
import bi.accounting.dto.AccountOpenIdDTO;
import bi.accounting.model.Account;
import bi.accounting.repository.AccountMemberRepository;
import bi.accounting.repository.AccountRepository;
import bi.accounting.service.AccountOauthService;
import bi.accounting.service.AccountService;
import bi.accounting.service.OAuthService;
import bi.accounting.service.TenantService;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.HttpStatus;
import jakarta.inject.Inject;
import io.micronaut.context.annotation.Value;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.UnsupportedEncodingException;

@Controller("/accounts")
@Secured(SecurityRule.IS_AUTHENTICATED)  // Ensure only authenticated users can access
public class AccountController {

    @Inject
    private AccountRepository accountRepository;
    @Inject
    private OAuthService oauthService;
    @Inject
    private TenantService tenantService;
    @Inject
    private AccountMemberRepository accountMemberRepository;
    @Inject
    private XeroAPI xeroAPI;
    @Inject
    private XeroClient xeroClient;
    @Inject
    private AccountService accountService;
    @Inject
    private AccountOauthService accountOauthService;
    @Inject
    private OpenIdClient openIdClient;


    @Value("${micronaut.server.base-url}")
    private String baseUrl;
    @Value("${micronaut.security.oauth.clients.xero.client-id}")
    private String clientId;
    @Value("${micronaut.security.oauth.clients.xero.client-secret}")
    private String clientSecret;
    @Value("${micronaut.security.oauth.clients.xero.authorization.url}")
    private String authorizationUrl;
    @Value("${micronaut.security.oauth.clients.xero.token.url}")
    private String tokenUrl;
    @Value("${micronaut.security.oauth.clients.xero.scopes}")
    private List<String> scopes;
    @Value("${micronaut.security.oauth.clients.xero.redirect-uri}")
    private String redirectUri;



    /* -----------------------   */

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get(uri = "/health", produces = "text/plain")
    public String index() {
        return "OK";
    }

    @Get("/")
    public List<AccountDTO> getAllAccounts(Authentication authentication) {
        String userIdString = (String) authentication.getAttributes().get("sub");
        Long userId = Long.parseLong(userIdString);

        return accountRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(account -> new AccountDTO(
                        account.getId(),
                        account.getOrgName(),
                        account.getOrgId(),
                        account.getProvider(),
                        account.getUserId(),
                        account.getDateCreated(),
                        account.getDateUpdated(),
                        account.getDeletedAt(),
                        account.getIsDeleted(),
                        account.getProviderId()
                ))
                .collect(Collectors.toList());
    }

    @Delete("/")
    public HttpResponse<?> deleteAccount(@Header("x-orgid") String orgId, Authentication authentication) {
        var account = accountRepository.findByOrgIdOrderByIdDesc(orgId);

        if (account == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        account.setIsDeleted(true);
        account.setDeletedAt(OffsetDateTime.now());
        accountRepository.update(account);

        AccountDTO deletedAccountDTO = new AccountDTO(
                account.getId(),
                account.getOrgName(),
                account.getOrgId(),
                account.getProvider(),
                account.getUserId(),
                account.getDateCreated(),
                account.getDateUpdated(),
                account.getDeletedAt(),
                account.getIsDeleted(),
                account.getProviderId()
        );

        return HttpResponse.ok(deletedAccountDTO);
    }

    @Get("{orgId}")
    public HttpResponse<?> getAccountByOrgId(@Header("x-UserId") String userIdHeader, String orgId) {
        Long userId = Long.parseLong(userIdHeader);

        Account account = accountRepository.findByOrgIdAndUserId(orgId, userId);

        if (account == null) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Account not found for the given OrgId and UserId");
        }

        AccountDTO getAccountDTO = new AccountDTO(
                account.getId(),
                account.getOrgName(),
                account.getOrgId(),
                account.getProvider(),
                account.getUserId(),
                account.getDateCreated(),
                account.getDateUpdated(),
                account.getDeletedAt(),
                account.getIsDeleted(),
                account.getProviderId()
        );
        return HttpResponse.ok(getAccountDTO);

    }

    @Get("/unsafe")
    public List<AccountDTO> getAllAccountsUnSafe(Authentication authentication) {
        String userIdString = (String) authentication.getAttributes().get("sub");
        Long userId = Long.parseLong(userIdString);

        return accountRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(account -> new AccountDTO(
                        account.getId(),
                        account.getOrgName(),
                        account.getOrgId(),
                        account.getProvider(),
                        account.getUserId(),
                        account.getDateCreated(),
                        account.getDateUpdated(),
                        account.getDeletedAt(),
                        account.getIsDeleted(),
                        account.getProviderId()
                ))
                .collect(Collectors.toList());
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/oauth/login/xero")
    public HttpResponse<?> loginWithXero(Long user) throws URISyntaxException, UnsupportedEncodingException {
        String scopeString = URLEncoder.encode(String.join(" ", scopes), StandardCharsets.UTF_8.toString());
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString());
        String encodedClientId = URLEncoder.encode(clientId, StandardCharsets.UTF_8.toString());
        String state = oauthService.generateState(user);

        String url = String.format(
                "%s?scope=%s&response_type=code&redirect_uri=%s&state=%s&client_id=%s",
                authorizationUrl,
                scopeString,
                encodedRedirectUri,
                state,
                encodedClientId
        );

        System.out.println(url);
        URI redirectUri = new URI(url);
        return HttpResponse.redirect(redirectUri);
    }



    @Secured(SecurityRule.IS_ANONYMOUS)
    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/openid/{provider}")
    public HttpResponse<?> handleOpenIdRequest(
            @PathVariable String provider,
            @Header("X-UserID") String userId,
            @Body HashMap<String, Object> body) {

        if ("xero".equalsIgnoreCase(provider)) {
            String accessToken = (String) body.get("access_token");
            System.out.println("accessToken:" + accessToken);
            HttpResponse<List<HashMap<String, Object>>> response = xeroAPI.getTenants("Bearer " + accessToken);

            if (response.getStatus().getCode() == 200) {
                List<HashMap<String, Object>> tenantList = response.body();
                List<AccountOpenIdDTO> accountOpenIdDTOList = tenantService.mapTenantsToDTO(tenantList);
                accountOpenIdDTOList.forEach(dto -> {
                    try {
                        Long insertedAccountId = accountService.insertAccount(dto, provider, Long.valueOf(userId));
                        accountOauthService.insertOrUpdateAccountOauth(insertedAccountId, body);

                    } catch (DataAccessException e) {
                        System.err.println("Failed to insert account or account OAuth for tenant: " + e.getMessage());
                    }
                });

                return HttpResponse.ok(accountOpenIdDTOList);
            } else {
                return HttpResponse.serverError("Failed to retrieve tenants from Xero");
            }
        }
        return HttpResponse.badRequest("Provider not supported: " + provider);
    }



    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/oauth/callback/xero")
    @ExecuteOn(TaskExecutors.BLOCKING)
    public MutableHttpResponse<?> handleXeroCallback(@QueryValue("code") Optional<String> code, @QueryValue("state") Optional<String> state) throws URISyntaxException {
        if (code.isPresent() && !code.get().isEmpty() && state.isPresent() && !state.get().isEmpty()) {
            String[] parts = state.get().split("__");
            Long userId = Long.parseLong(parts[1]);
            System.out.println(userId);

            String idTokenRequestBody = "grant_type=authorization_code&code=" + code.get() + "&redirect_uri=" + redirectUri;
            HttpResponse<HashMap<?, ?>> tokenResponse = xeroClient.idToken("Basic " + oauthService.getBasicAuthHeader(clientId, clientSecret), idTokenRequestBody);
            if (tokenResponse.getStatus().getCode() == 200) {
                HashMap<?, ?> tokenBody = tokenResponse.body();
                String accessToken = (String) tokenBody.get("access_token");
                System.out.println("accessToken: "+accessToken);

                HttpResponse<List<HashMap<String, Object>>> tenantResponse = xeroAPI.getTenants("Bearer " + accessToken);

                if (tenantResponse.getStatus().getCode() == 200) {
                    List<HashMap<String, Object>> tenantList = tenantResponse.body();
                    List<AccountOpenIdDTO> accountOpenIdDTOList = tenantService.mapTenantsToDTO(tenantList);
                    accountOpenIdDTOList.forEach(dto -> {
                        try {
                            Long insertedAccountId = accountService.insertAccount(dto, "xero", userId);
                            accountOauthService.insertOrUpdateAccountOauth(insertedAccountId, tokenBody);
                        } catch (Exception e) {
                            System.err.println("Failed to insert account or account OAuth for tenant: " + e.getMessage());
                        }
                    });

                    String redirectUrl = baseUrl + "/dashboard/organisations";
                    return HttpResponse.redirect(new URI(redirectUrl));
                } else {
                    return HttpResponse.serverError("Failed to fetch tenants from Xero");
                }
            } else {
                return HttpResponse.serverError("Failed to exchange authorization code for token");
            }
        }
        else {
            String redirectUrl = baseUrl + "/dashboard/organisations";
            return HttpResponse.redirect(new URI(redirectUrl));
        }
    }

}
