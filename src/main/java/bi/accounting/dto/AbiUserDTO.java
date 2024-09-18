package bi.accounting.dto;

import io.micronaut.core.annotation.Introspected;
import java.time.OffsetDateTime;

@Introspected
public class AbiUserDTO {

    private Long id;
    private String email;
    private String username;
    private Boolean isEnabled;
    private Boolean isAccountExpired;
    private Boolean isAccountLocked;
    private Boolean isPasswordExpired;
    private OffsetDateTime dateCreated;
    private OffsetDateTime dateUpdated;

    // Constructors
    public AbiUserDTO() {
    }

    public AbiUserDTO(Long id, String email, String username, Boolean isEnabled, Boolean isAccountExpired, Boolean isAccountLocked, Boolean isPasswordExpired, OffsetDateTime dateCreated, OffsetDateTime dateUpdated) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.isEnabled = isEnabled;
        this.isAccountExpired = isAccountExpired;
        this.isAccountLocked = isAccountLocked;
        this.isPasswordExpired = isPasswordExpired;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsAccountExpired() {
        return isAccountExpired;
    }

    public void setIsAccountExpired(Boolean isAccountExpired) {
        this.isAccountExpired = isAccountExpired;
    }

    public Boolean getIsAccountLocked() {
        return isAccountLocked;
    }

    public void setIsAccountLocked(Boolean isAccountLocked) {
        this.isAccountLocked = isAccountLocked;
    }

    public Boolean getIsPasswordExpired() {
        return isPasswordExpired;
    }

    public void setIsPasswordExpired(Boolean isPasswordExpired) {
        this.isPasswordExpired = isPasswordExpired;
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
}
