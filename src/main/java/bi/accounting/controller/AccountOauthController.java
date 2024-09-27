package bi.accounting.controller;

import bi.accounting.dto.AccountOauthDTO;
import bi.accounting.model.Account;
import bi.accounting.model.AccountOauth;
import bi.accounting.repository.AccountOauthRepository;
import bi.accounting.repository.AccountRepository;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.MediaType;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.Optional;

@Controller("/accounts/token")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AccountOauthController {

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private AccountOauthRepository accountOauthRepository;

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
}
