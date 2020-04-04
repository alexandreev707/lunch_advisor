package ru.lunch.advisor.web.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.service.dto.UserDTO;
import ru.lunch.advisor.web.request.UserUpdateRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserWebMapperTest {

    private UserWebMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(UserWebMapper.class);
    }

    @Test
    public void map() {
        UserUpdateRequest request = new UserUpdateRequest("Тестовый", true, "test@test.ru",
                Collections.singletonList(Role.ROLE_USER), "Password1");
        UserDTO dto = mapper.map(request);

        assertEquals("test@test.ru", dto.getEmail());
        assertEquals("Password1", dto.getPassword());
        assertEquals(Collections.singletonList(Role.ROLE_USER), dto.getRoles());
        assertEquals("Тестовый", dto.getName());
    }
}
