package bi.accounting.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Id;

import java.time.OffsetDateTime;

@Introspected
@MappedEntity(value = "account", schema = "accounts_schema")
public class Account {

    @Id
    private Long id;  // bigint corresponds to Long

    private String orgName;        // character varying corresponds to String
    private String orgId;          // character varying corresponds to String
    private String provider;       // character varying corresponds to String
    private Long userId;           // bigint corresponds to Long
    private OffsetDateTime dateCreated;  // timestamp with time zone corresponds to OffsetDateTime
    private OffsetDateTime dateUpdated;  // timestamp with time zone corresponds to OffsetDateTime
    private OffsetDateTime deletedAt;    // timestamp with time zone corresponds to OffsetDateTime
    private Boolean isDeleted;     // boolean corresponds to Boolean
    private String providerId;     // character varying corresponds to String

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

    public void setProvider(String provider) {
        this.provider = provider;
    }

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
