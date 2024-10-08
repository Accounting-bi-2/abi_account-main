package bi.accounting.dto;


import io.micronaut.core.annotation.Introspected;

@Introspected
public class AccountOpenIdDTO {
    private String id;
    private String tenantId;
    private String tenantType;
    private String tenantName;
    private String createdDateUtc;
    private String updatedDateUtc;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantType() {
        return tenantType;
    }

    public void setTenantType(String tenantType) {
        this.tenantType = tenantType;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCreatedDateUtc() {
        return createdDateUtc;
    }

    public void setCreatedDateUtc(String createdDateUtc) {
        this.createdDateUtc = createdDateUtc;
    }

    public String getUpdatedDateUtc() {
        return updatedDateUtc;
    }

    public void setUpdatedDateUtc(String updatedDateUtc) {
        this.updatedDateUtc = updatedDateUtc;
    }
}
