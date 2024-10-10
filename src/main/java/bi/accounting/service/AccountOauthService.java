package bi.accounting.service;

import bi.accounting.model.AccountOauth;
import bi.accounting.repository.AccountOauthRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class AccountOauthService {

    private final AccountOauthRepository accountOauthRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AccountOauthService.class);

    public AccountOauthService(AccountOauthRepository accountOauthRepository) {
        this.accountOauthRepository = accountOauthRepository;
    }

    @Transactional
    public void insertOrUpdateAccountOauth(Long accountId, HashMap<?, ?> body) {
        LOG.info("insertOrUpdateAccountOauth: {}", accountId);

        Optional<AccountOauth> existingAccountOauthOpt = accountOauthRepository.findByAccountIdAndIsDeletedFalse(accountId);

        if (existingAccountOauthOpt.isPresent()) {
            AccountOauth accountOauth = existingAccountOauthOpt.get();
            updateAccountOauthFields(accountOauth, body);
            accountOauth.setDateUpdated(OffsetDateTime.now());
            accountOauth.setIsDeleted(false);
            accountOauthRepository.update(accountOauth);
            LOG.info("Existing AccountOauth updated for accountId: {}", accountId);
        } else {
            AccountOauth accountOauth = new AccountOauth();
            accountOauth.setAccountId(accountId);
            accountOauth.setDateCreated(OffsetDateTime.now());
            updateAccountOauthFields(accountOauth, body);
            accountOauth.setIsDeleted(false);
            accountOauthRepository.save(accountOauth);
            LOG.info("New AccountOauth inserted for accountId: {}", accountId);
        }
    }

    // Helper method to update fields
    private void updateAccountOauthFields(AccountOauth accountOauth, HashMap<?, ?> body) {
        accountOauth.setAccessToken((String) body.get("access_token"));
        accountOauth.setRefreshToken((String) body.get("refresh_token"));
        accountOauth.setExpiresIn(((Number) body.get("expires_in")).longValue());
        accountOauth.setScope((String) body.get("scope"));
        accountOauth.setTokenType((String) body.get("token_type"));
    }
}