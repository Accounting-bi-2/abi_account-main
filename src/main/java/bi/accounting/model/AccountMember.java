package bi.accounting.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Id;

@Introspected
@MappedEntity(value = "account_member", schema = "accounts_schema")
public class AccountMember {

    @Id
    @GeneratedValue
    private Long id;

    private String orgId;  // Changed from org_id to orgId
    private Long userId;   // Changed from user_id to userId
    private Boolean isDeleted; // Changed from is_deleted to isDeleted

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
