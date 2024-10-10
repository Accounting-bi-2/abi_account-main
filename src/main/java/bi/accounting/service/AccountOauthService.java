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

    public void insertAccountOauth(Long accountId, HashMap<String, Object> body) {
        AccountOauth accountOauth = new AccountOauth();
        accountOauth.setAccountId(accountId);
        accountOauth.setAccessToken((String) body.get("access_token"));
        accountOauth.setRefreshToken((String) body.get("refresh_token"));
        accountOauth.setExpiresIn(((Number) body.get("expires_in")).longValue());
        accountOauth.setScope((String) body.get("scope"));
        accountOauth.setTokenType((String) body.get("token_type"));
        accountOauth.setDateCreated(OffsetDateTime.now());
        accountOauth.setDateUpdated(OffsetDateTime.now());
        accountOauth.setIsDeleted(false);

        accountOauthRepository.save(accountOauth);
    }
}