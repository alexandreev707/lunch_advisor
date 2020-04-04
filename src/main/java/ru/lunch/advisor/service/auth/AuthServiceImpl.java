package ru.lunch.advisor.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lunch.advisor.service.UserService;

/**
 * Сервис для ацтентификации пользователя
 */
@Service("authServiceImpl")
public class AuthServiceImpl implements UserDetailsService {

    private final UserService userService;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new AuthUser(userService.getByEmail(email));
    }
}
