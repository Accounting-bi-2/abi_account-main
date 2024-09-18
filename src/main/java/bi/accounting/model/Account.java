package bi.accounting.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Id;

import java.time.OffsetDateTime;

@Introspected
@MappedEntity(value = "account", schema = "accounts_schema")
public class Account {

    @Id
    private Long id;

    private String orgName;
    private String orgId;
    private String provider;
    private Long userId;
    private OffsetDateTime dateCreated;
    private OffsetDateTime dateUpdated;
    private OffsetDateTime deletedAt;
    private Boolean isDeleted;
    private String providerId;

    // Getters and setters
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

    public void setProvider(String provider) { this.provider = provider;}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(OffsetDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
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
