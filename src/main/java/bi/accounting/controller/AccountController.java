package bi.accounting.controller;

import bi.accounting.dto.AccountDTO;
import bi.accounting.repository.AccountRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.authentication.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/accounts")
@Secured(SecurityRule.IS_AUTHENTICATED)  // Ensure only authenticated users can access
public class AccountController {

    @Inject
    private AccountRepository accountRepository;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get(uri = "/health", produces = "text/plain")
    public String index() {
        return "OK";
    }

    @Operation(summary = "Get greeting message")
    @Get("/")
    public List<AccountDTO> getAllAccounts(Authentication authentication) {
        String userIdString = (String) authentication.getAttributes().get("sub");
        Long userId = Long.parseLong(userIdString);

        return accountRepository.findByUserId(userId)
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

    @Get("/{id}")
    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id)
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
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Get("/user/{userId}")
    public List<AccountDTO> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
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


}
