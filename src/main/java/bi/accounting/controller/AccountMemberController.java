package bi.accounting.controller;

import bi.accounting.dto.AccountMemberDTO;
import bi.accounting.model.AccountMember;
import bi.accounting.repository.AccountMemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller("/accounts/members")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AccountMemberController {

    @Inject
    private AccountMemberRepository accountMemberRepository;
    @Inject
    private ObjectMapper objectMapper;

    @Post
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<List<AccountMemberDTO>> addAccountMember(
            @Header("X-UserID") Long userId,
            @Body String accountMemberRequest) {

        System.out.println(accountMemberRequest);
        try {
            Map<String, Object> requestMap = objectMapper.readValue(accountMemberRequest, new TypeReference<Map<String, Object>>() {});
            List<String> orgIds = (List<String>) requestMap.get("orgIds");
            Long memberId = Long.parseLong((String) requestMap.get("memberId"));

            List<AccountMemberDTO> accountMembers = new ArrayList<>();

            for (String orgId : orgIds) {

                AccountMember accountMember = new AccountMember();
                accountMember.setOrgId(orgId);
                accountMember.setUserId(memberId);
                accountMember.setIsDeleted(false);

                System.out.println("Saving AccountMember with UserId: " + accountMember.getUserId());

                accountMemberRepository.save(accountMember);

                AccountMemberDTO accountMemberDTO = new AccountMemberDTO(
                        accountMember.getId(),
                        orgId,
                        memberId,
                        false
                );

                accountMembers.add(accountMemberDTO);
            }

            return HttpResponse.ok(accountMembers);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return HttpResponse.badRequest(Collections.emptyList());
        }
    }


}