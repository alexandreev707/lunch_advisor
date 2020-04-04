package ru.lunch.advisor.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.persistence.model.UserModel;
import ru.lunch.advisor.service.dto.UserDTO;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    public void map() {
        UserModel model = stubUserModel();
        UserDTO dto = mapper.map(model);

        assertEquals(97L, dto.getId());
        assertEquals("user_test@test.com", dto.getEmail());
        assertEquals("Test pass7", dto.getPassword());
        assertEquals(Collections.singletonList(Role.ROLE_ADMIN), dto.getRoles());
        assertEquals("User_test", dto.getName());
        assertTrue(dto.isEnabled());
    }

    @Test
    public void mapToList() {
        UserModel model = stubUserModel();
        List<UserDTO> dto = mapper.map(Collections.singletonList(model));

        assertNotNull(dto);
        assertEquals(97L, dto.get(0).getId());
        assertEquals("user_test@test.com", dto.get(0).getEmail());
        assertEquals("Test pass7", dto.get(0).getPassword());
        assertEquals(Collections.singletonList(Role.ROLE_ADMIN), dto.get(0).getRoles());
        assertEquals("User_test", dto.get(0).getName());
        assertTrue(dto.get(0).isEnabled());
    }

    private UserModel stubUserModel() {
        UserModel userModel = new UserModel();
        userModel.setId(97L);
        userModel.setEmail("user_test@test.com");
        userModel.setName("User_test");
        userModel.setPassword("Test pass7");
        userModel.setEnabled(true);
        userModel.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN)));

        return userModel;
    }
}
