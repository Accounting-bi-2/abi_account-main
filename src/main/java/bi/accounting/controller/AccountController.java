package bi.accounting.controller;

import bi.accounting.dto.AccountDTO;
import bi.accounting.repository.AccountRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.authentication.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Header;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;
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
        var account = accountRepository.findByOrgId(orgId);

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



}
