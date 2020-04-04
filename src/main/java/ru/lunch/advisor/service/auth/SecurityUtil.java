package ru.lunch.advisor.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * Получение аутентифицированного пользователя
     */
    public static AuthUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthUser) ? (AuthUser) principal : null;
    }

    /**
     * Получение аутентифицированного пользователя с проверкой
     */
    public static AuthUser get() {
        AuthUser user = safeGet();
        requireNonNull(user, "User not found");
        return user;
    }

    /**
     * Получить ид аутентифицированного пользователя с предварительной проверкой
     */
    public static long authUserId() {
        return get().getUserId();
    }
}