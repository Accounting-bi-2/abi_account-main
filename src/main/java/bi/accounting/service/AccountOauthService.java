package bi.accounting.service;

import bi.accounting.model.AccountOauth;
import bi.accounting.repository.AccountOauthRepository;
import jakarta.inject.Singleton;
import java.time.OffsetDateTime;
import java.util.HashMap;

@Singleton
public class AccountOauthService {

    private final AccountOauthRepository accountOauthRepository;

    public AccountOauthService(AccountOauthRepository accountOauthRepository) {
        this.accountOauthRepository = accountOauthRepository;
    }

    public void insertOrUpdateAccountOauth(Long accountId, HashMap<?, ?> body) {
        System.out.println("insertOrUpdateAccountOauth: " + accountId);
        AccountOauth accountOauth = accountOauthRepository.findByAccountId(accountId);

        if (accountOauth == null) {
            accountOauth = new AccountOauth();
            accountOauth.setAccountId(accountId);
            accountOauth.setDateCreated(OffsetDateTime.now());
            accountOauth.setAccessToken((String) body.get("access_token"));
            accountOauth.setRefreshToken((String) body.get("refresh_token"));
            accountOauth.setExpiresIn(((Number) body.get("expires_in")).longValue());
            accountOauth.setScope((String) body.get("scope"));
            accountOauth.setTokenType((String) body.get("token_type"));
            accountOauth.setDateUpdated(OffsetDateTime.now());
            accountOauth.setIsDeleted(false);
            accountOauthRepository.save(accountOauth);
            System.out.println("New AccountOauth inserted");
        } else {
            accountOauth.setAccessToken((String) body.get("access_token"));
            accountOauth.setRefreshToken((String) body.get("refresh_token"));
            accountOauth.setExpiresIn(((Number) body.get("expires_in")).longValue());
            accountOauth.setScope((String) body.get("scope"));
            accountOauth.setTokenType((String) body.get("token_type"));
            accountOauth.setDateUpdated(OffsetDateTime.now());
            accountOauth.setIsDeleted(false);
            accountOauthRepository.update(accountOauth);
            System.out.println("Existing AccountOauth updated");
        }
    }

}