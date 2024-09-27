package bi.accounting.dto;

import io.micronaut.core.annotation.Introspected;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Introspected
public class AccountOauthDTO {

    private Long id;
    private String orgName;
    private String orgId;
    private String provider;
    private String providerId;
    private Long userId;
    private Boolean isDeleted;
    private String dateCreated;
    private String dateUpdated;
    private String accessToken;

    public AccountOauthDTO(Long id, String orgName, String orgId, String provider, String providerId, Long userId, Boolean isDeleted, OffsetDateTime dateCreated, OffsetDateTime dateUpdated, String accessToken) {
        this.id = id;
        this.orgName = orgName;
        this.orgId = orgId;
        this.provider = provider;
        this.providerId = providerId;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.dateCreated = dateCreated != null ? dateCreated.toInstant().toString() : null;
        this.dateUpdated = dateUpdated != null ? dateUpdated.toInstant().toString() : null;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public Long getUserId() {
        return userId;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
