package bi.accounting.service;

import bi.accounting.dto.AccountOpenIdDTO;
import bi.accounting.model.Account;
import bi.accounting.repository.AccountRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Optional;

@Singleton
public class AccountService {

    private final AccountRepository accountRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Long insertAccount(AccountOpenIdDTO dto, String provider, Long userId) {
        LOG.info("Searching for account with orgId: {} and userId: {}", dto.getTenantId(), userId);

        Optional<Account> existingAccountOpt = accountRepository.findByOrgIdAndUserIdAndIsDeletedFalse(dto.getTenantId(), userId);

        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();
            LOG.info("Account already exists for userId: {} and tenantId: {}", userId, dto.getTenantId());
            LOG.info("Account ID: {}", existingAccount.getId());
            return existingAccount.getId();
        }

        Account account = new Account();
        account.setOrgName(dto.getTenantName());
        account.setOrgId(dto.getTenantId());
        account.setProviderId(dto.getId());
        account.setProvider(provider);
        account.setUserId(userId);
        account.setDateCreated(OffsetDateTime.now());
        account.setDateUpdated(OffsetDateTime.now());
        account.setIsDeleted(false);

        LOG.info("Inserting account with details: {}", account);
        try {
            accountRepository.save(account);
            LOG.info("Account inserted successfully with ID: {}", account.getId());
        } catch (Exception e) {
            LOG.error("Failed to insert account: {}", e.getMessage(), e);
            // Optionally, you can rethrow the exception or return a specific value to indicate failure
            throw new RuntimeException("Failed to insert account", e);
        }

        return account.getId();
    }
}
