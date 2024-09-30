package bi.accounting.controller;

import bi.accounting.client.XeroClient;
import bi.accounting.dto.AccountOauthDTO;
import bi.accounting.model.Account;
import bi.accounting.model.AccountOauth;
import bi.accounting.repository.AccountOauthRepository;
import bi.accounting.repository.AccountRepository;
import bi.accounting.service.OAuthService;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.*;

@Controller("/accounts/token")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AccountOauthController {

    @Inject
    private AccountRepository accountRepository;
    @Inject
    private XeroClient xeroClient;
    @Inject
    private OAuthService oauthService;
    @Inject
    private AccountOauthRepository accountOauthRepository;

    @Value("${micronaut.security.oauth.clients.xero.client-id}")
    private String clientId;
    @Value("${micronaut.security.oauth.clients.xero.client-secret}")
    private String clientSecret;

    private static final Logger LOG = LoggerFactory.getLogger(AccountOauthController.class);



    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<AccountOauthDTO> getAccountToken(
            @Header("X-UserID") Long userId,
            @Header("X-OrgID") String orgId) {

        Account account = accountRepository.findByOrgIdAndUserId(orgId, userId);

        if (account != null) {
            AccountOauth accountOauth = accountOauthRepository.findByAccountId(account.getId());

            if (accountOauth != null) {
                AccountOauthDTO response = new AccountOauthDTO(
                        account.getId(),
                        account.getOrgName(),
                        account.getOrgId(),
                        account.getProvider(),
                        account.getProviderId(),
                        account.getUserId(),
                        account.getIsDeleted(),
                        account.getDateCreated(),
                        account.getDateUpdated(),
                        accountOauth.getAccessToken()
                );

                return HttpResponse.ok(response);
            }
        }

        return HttpResponse.notFound();
    }




    @Get("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_ANONYMOUS)
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<?> refreshTokens() {
        List<AccountOauth> oauthAccounts = accountOauthRepository.findAllByIsDeleted(false);
        int successCount = 0;

        for (AccountOauth accountOauth : oauthAccounts) {
            try {
                String refreshToken = accountOauth.getRefreshToken();
                String requestBody = "grant_type=refresh_token" +
                        "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8) +
                        "&client_id=" + clientId;

                HttpResponse<HashMap<?, ?>> response = xeroClient.idToken("Basic " + oauthService.getBasicAuthHeader(clientId, clientSecret), requestBody);

                if (response.getStatus().getCode() == 200) {
                    HashMap<?, ?> responseBody = response.body();
                    accountOauth.setAccessToken((String) responseBody.get("access_token"));
                    accountOauth.setRefreshToken((String) responseBody.get("refresh_token"));
                    accountOauth.setExpiresIn(Long.valueOf(responseBody.get("expires_in").toString()));
                    accountOauth.setScope((String) responseBody.get("scope"));
                    accountOauth.setTokenType((String) responseBody.get("token_type"));
                    accountOauth.setDateUpdated(OffsetDateTime.now());

                    accountOauthRepository.update(accountOauth);
                    successCount++;
                } else {
                    LOG.error("Failed to refresh token for accountId {}: Missing access token in response", accountOauth.getAccountId());
                }
            } catch (HttpClientResponseException e) {
                LOG.error("Failed to refresh token for accountId {}: {}", accountOauth.getAccountId(), e.getStatus());
            } catch (Exception e) {
                LOG.error("Error refreshing token for accountId {}: {}", accountOauth.getAccountId(), e.getMessage(), e);
            }
        }

        return HttpResponse.ok(Map.of("message", successCount + " Tokens refreshed successfully"));
    }




}
