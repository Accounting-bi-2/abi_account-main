package bi.accounting.service;

import bi.accounting.dto.AccountOpenIdDTO;
import bi.accounting.model.Account;
import bi.accounting.repository.AccountRepository;
import jakarta.inject.Singleton;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.logging.Logger;

@Singleton
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Long insertAccount(AccountOpenIdDTO dto, String provider, Long userId) {
        Account account = new Account();
        account.setOrgName(dto.getTenantName());
        account.setOrgId(dto.getTenantId());
        account.setProviderId(dto.getId());
        account.setProvider(provider);
        account.setUserId(userId);
        account.setDateCreated(OffsetDateTime.now());
        account.setDateUpdated(OffsetDateTime.now());
        account.setIsDeleted(false);

        String insertQuery = String.format(
                "INSERT INTO account (org_name, org_id, provider_id, provider, user_id, date_created, date_updated, is_deleted) " +
                        "VALUES ('%s', '%s', '%s', '%s', %d, '%s', '%s', %b)",
                account.getOrgName(),
                account.getOrgId(),
                account.getProviderId(),
                account.getProvider(),
                account.getUserId(),
                account.getDateCreated(),
                account.getDateUpdated(),
                account.getIsDeleted()
        );

        // Log the constructed SQL insert query
        System.out.println(insertQuery);

        accountRepository.save(account);


        return account.getId();
    }
}