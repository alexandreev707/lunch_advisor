package ru.lunch.advisor.persistence.query.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.persistence.model.UserModel;
import ru.lunch.advisor.service.exeption.NotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAll() {
        assertEquals(5, userRepository.findAll().size());
    }

    @Test
    public void get() {
        UserModel user = userRepository.findById(50L).orElse(null);

        assertNotNull(user);
        assertEquals(Long.valueOf(50), user.getId());
        assertEquals("Ivanov", user.getName());
        assertEquals("ivanov@test.ru", user.getEmail());
        assertEquals("$2a$10$K0HHkSmwzoKETH77OvFxvOddqbzCC.w9gLWyRXkxM8u/UXSGfKhrm", user.getPassword());
        assertEquals(Collections.singleton(Role.ROLE_USER), user.getRoles());
    }

    @Test
    public void getByEmail() {
        UserModel user = userRepository.getByEmail("ivanov@test.ru").orElse(null);

        assertNotNull(user);
        assertEquals(Long.valueOf(50), user.getId());
        assertEquals("Ivanov", user.getName());
        assertEquals("ivanov@test.ru", user.getEmail());
        assertEquals("$2a$10$K0HHkSmwzoKETH77OvFxvOddqbzCC.w9gLWyRXkxM8u/UXSGfKhrm", user.getPassword());
        assertEquals(Collections.singleton(Role.ROLE_USER), user.getRoles());
    }

    @Rollback
    @Test
    public void create() {
        UserModel userExpected = new UserModel();
        userExpected.setEnabled(true);
        userExpected.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_ADMIN, Role.ROLE_USER)));
        userExpected.setPassword("Test pass7");
        userExpected.setName("User_test");
        userExpected.setEmail("user_test@test.com");

        UserModel userNew = userRepository.save(userExpected);

        assertNotNull(userExpected);
        assertEquals(userExpected.getName(), userNew.getName());
        assertEquals(userExpected.getEmail(), userNew.getEmail());
        assertEquals(userExpected.getPassword(), userNew.getPassword());
        assertEquals(userExpected.getRoles(), userNew.getRoles());
    }

    @Rollback
    @Test
    public void update() {
        UserModel user = userRepository.findById(52L).orElseThrow(() -> new NotFoundException(52L));
        assertEquals("petrov@test.ru", user.getEmail());
        assertTrue(user.isEnabled());
        assertEquals("petrov", user.getName());
        assertEquals("Password1", user.getPassword());
        assertEquals(Collections.emptySet(), user.getRoles());

        UserModel expected = userUpdated();
        user = userRepository.save(expected);
        assertEquals(expected.getEmail(), user.getEmail());
        assertFalse(user.isEnabled());
        assertEquals(expected.getName(), user.getName());
        assertEquals(expected.getPassword(), user.getPassword());
        assertEquals(expected.getRoles(), user.getRoles());
    }

    private UserModel userUpdated() {
        UserModel user = new UserModel();
        user.setEmail("test@test.ru");
        user.setId(52L);
        user.setEnabled(false);
        user.setName("Vivaldi");
        user.setPassword("Vivaldi 1");
        user.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN)));

        return user;
    }

    @Rollback
    @Test
    public void remove() {
        assertEquals(5, userRepository.findAll().size());
        userRepository.deleteById(54L);
        assertEquals(4, userRepository.findAll().size());
    }
}
