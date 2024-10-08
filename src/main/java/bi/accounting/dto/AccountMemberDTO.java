package bi.accounting.dto;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public class AccountMemberDTO {

    private Long id;
    private String orgId;
    private Long userId;
    private Boolean isDeleted;

    public AccountMemberDTO(Long id, String orgId, Long userId, Boolean isDeleted) {
        this.id = id;
        this.orgId = orgId;
        this.userId = userId;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
