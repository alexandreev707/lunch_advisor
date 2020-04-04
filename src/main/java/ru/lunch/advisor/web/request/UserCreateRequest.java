package ru.lunch.advisor.web.request;

import ru.lunch.advisor.persistence.model.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UserCreateRequest {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Boolean enabled;
    @NotBlank
    @Email
    private String email;
    @NotEmpty
    private List<Role> roles;
    @NotBlank
    private String password;

    public UserCreateRequest() {
    }

    public UserCreateRequest(String name, Boolean enabled, String email, List<Role> roles, String password) {
        this.name = name;
        this.enabled = enabled;
        this.email = email;
        this.roles = roles;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
