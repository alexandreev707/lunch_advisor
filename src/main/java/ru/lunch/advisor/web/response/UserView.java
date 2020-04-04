package ru.lunch.advisor.web.response;

import org.springframework.util.CollectionUtils;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.service.dto.UserDTO;

import java.util.List;
import java.util.Objects;

public class UserView {

    private Long id;
    private String name;
    private String email;
    private List<Role> roles;
    private Boolean enabled;

    public UserView() {
    }

    public UserView(Long id, String name, String email, List<Role> roles, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.enabled = enabled;
    }

    public UserView(UserDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.email = dto.getEmail();
        if (!CollectionUtils.isEmpty(dto.getRoles()))
            this.roles = dto.getRoles();
        this.enabled = dto.isEnabled();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserView userView = (UserView) o;
        return Objects.equals(id, userView.id) &&
                Objects.equals(name, userView.name) &&
                Objects.equals(email, userView.email) &&
                Objects.equals(roles, userView.roles) &&
                Objects.equals(enabled, userView.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, roles, enabled);
    }
}
