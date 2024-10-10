package bi.accounting.controller;

import bi.accounting.client.OpenIdClient;
import bi.accounting.client.XeroAPI;
import bi.accounting.dto.AccountDTO;
import bi.accounting.dto.AccountOpenIdDTO;
import bi.accounting.model.Account;
import bi.accounting.repository.AccountMemberRepository;
import bi.accounting.repository.AccountRepository;
import bi.accounting.service.AccountOauthService;
import bi.accounting.service.AccountService;
import bi.accounting.service.OAuthService;
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
    private AccountMemberRepository accountMemberRepository;
    @Inject
    private XeroAPI xeroAPI;
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
    public HttpResponse<?> loginWithXero(String user) throws URISyntaxException, UnsupportedEncodingException {
        String scopeString = URLEncoder.encode(String.join(" ", scopes), StandardCharsets.UTF_8.toString());
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString());
        String encodedClientId = URLEncoder.encode(clientId, StandardCharsets.UTF_8.toString());
        String state = oauthService.generateState();

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
                List<AccountOpenIdDTO> accountOpenIdDTOList = tenantList.stream()
                        .filter(tenant -> "ORGANISATION".equalsIgnoreCase((String) tenant.get("tenantType"))) // Filter by ORGANISATION
                        .map(tenant -> {
                            AccountOpenIdDTO dto = new AccountOpenIdDTO();
                            dto.setId((String) tenant.get("id"));
                            dto.setTenantId((String) tenant.get("tenantId"));
                            dto.setTenantType((String) tenant.get("tenantType"));
                            dto.setTenantName((String) tenant.get("tenantName"));
                            dto.setCreatedDateUtc((String) tenant.get("createdDateUtc"));
                            dto.setUpdatedDateUtc((String) tenant.get("updatedDateUtc"));
                            return dto;
                        }).collect(Collectors.toList());

                accountOpenIdDTOList.forEach(dto -> {
                    try {
                        Long insertedAccountId = accountService.insertAccount(dto, provider, Long.valueOf(userId));
                        accountOauthService.insertAccountOauth(insertedAccountId, body);

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
    public Mono<MutableHttpResponse<Object>> handleXeroCallback(@QueryValue Optional<String> code) {
        if (code.isPresent()) {
            // If the code is provided, use it to get the token
            return openIdClient.getToken("xero", code.get())
                    .flatMap(token -> {
                        System.out.println("Received Token: " + token);

                        String redirectUrl = baseUrl + "/dashboard/organisations";
                        try {
                            return Mono.just(HttpResponse.redirect(new URI(redirectUrl)));
                        } catch (URISyntaxException e) {
                            return Mono.error(new RuntimeException("Failed to redirect", e));
                        }
                    })
                    .onErrorResume(throwable -> {
                        // Handle error case
                        System.err.println("Error during token exchange: " + throwable.getMessage());
                        return Mono.just(HttpResponse.serverError("Failed to retrieve token"));
                    });
        } else {
            // If the code is not provided, just redirect to the dashboard
            String redirectUrl = baseUrl + "/dashboard/organisations";
            try {
                return Mono.just(HttpResponse.redirect(new URI(redirectUrl)));
            } catch (URISyntaxException e) {
                return Mono.error(new RuntimeException("Failed to redirect", e));
            }
        }
    }


}
