package bi.accounting.dto;

import io.micronaut.core.annotation.Introspected;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Introspected
public class AccountDTO {

    private Long id;
    private String orgName;
    private String orgId;
    private String provider;
    private Long userId;
    private String dateCreated;
    private String dateUpdated;
    private String deletedAt;
    private Boolean isDeleted;
    private String providerId;

    public AccountDTO(Long id, String orgName, String orgId, String provider, Long userId, OffsetDateTime dateCreated, OffsetDateTime dateUpdated, OffsetDateTime deletedAt, Boolean isDeleted, String providerId) {
        this.id = id;
        this.orgName = orgName;
        this.orgId = orgId;
        this.provider = provider;
        this.userId = userId;
        this.dateCreated = dateCreated != null ? dateCreated.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        this.dateUpdated = dateUpdated != null ? dateUpdated.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        this.deletedAt = deletedAt != null ? deletedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        this.isDeleted = isDeleted;
        this.providerId = providerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
