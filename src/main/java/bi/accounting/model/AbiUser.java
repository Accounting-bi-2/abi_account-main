package bi.accounting.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Id;

import java.time.OffsetDateTime;

@Introspected
@MappedEntity(value = "abi_user", schema = "users_schema")
public class AbiUser {

    @Id
    private Long id;

    private String email;
    private String username;
    private String password;
    private Boolean isEnabled;
    private Boolean isAccountExpired;
    private Boolean isAccountLocked;
    private Boolean isPasswordExpired;
    private OffsetDateTime dateCreated;
    private OffsetDateTime dateUpdated;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
