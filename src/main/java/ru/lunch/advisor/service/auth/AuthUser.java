package ru.lunch.advisor.service.auth;

import org.springframework.security.core.userdetails.User;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.service.dto.UserDTO;

/**
 * Класс для аутентифицированного/авторизованного пользователя
 */
public class AuthUser extends User {

    private UserDTO user;

    public AuthUser(UserDTO user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true,
                true, user.getRoles());
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }

    public boolean isAdmin() {
        return user.getRoles().contains(Role.ROLE_ADMIN);
    }
}
