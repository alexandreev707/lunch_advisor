package ru.lunch.advisor.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.lunch.advisor.TestUtils;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.service.UserService;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.web.controller.rest.UserRestController;
import ru.lunch.advisor.web.json.JsonUtil;
import ru.lunch.advisor.web.request.UserUpdateRequest;
import ru.lunch.advisor.web.response.UserMenuView;
import ru.lunch.advisor.web.response.UserView;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@Transactional
class UserRestControllerIT {

    private static final String REST_URL = UserRestController.USER_URL + "/";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserService userService;

    @BeforeEach
    void init() {
        initMocks(this);

        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(encodingFilter)
                .apply(springSecurity())
                .build();
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TestUtils.contentJson(stubUserView(), UserView.class));
    }

    @Test
    void all() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<UserView> users = TestUtils.readListFromJsonMvcResult(result, UserView.class);
        assertEquals(5, users.size());
    }

    @Test
    void menu() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "menu")
                .param("startDate", LocalDate.now().toString())
                .param("endDate", LocalDate.now().toString())
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<UserMenuView> users = TestUtils.readListFromJsonMvcResult(result, UserMenuView.class);
        assertEquals(3, users.size());
        UserMenuView userMenuView = users.get(0);
        assertEquals(53L, userMenuView.getId());
        assertEquals("Lunch", userMenuView.getMenu());
        assertEquals("SUNRISE", userMenuView.getRestaurant());
        assertEquals(false, userMenuView.getVote());
    }

    @Rollback
    @Test
    void create() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("Надежда", true, "test@test.ru",
                Collections.singletonList(Role.ROLE_USER), "test123");

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "register")
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isCreated());

        UserView created = TestUtils.readFromJson(action, UserView.class);
        UserView expected = new UserView(created.getId(), "Надежда", "test@test.ru",
                Collections.singletonList(Role.ROLE_USER), true);

        TestUtils.assertMatch(expected.getId(), created.getId());
        TestUtils.assertMatch(expected.getEmail(), created.getEmail());
        TestUtils.assertMatch(expected.getEnabled(), created.getEnabled());
        TestUtils.assertMatch(expected.getName(), created.getName());
        TestUtils.assertMatch(expected.getRoles(), created.getRoles());
    }

    @Rollback
    @Test
    void update() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        UserView oldUser = TestUtils.readFromJson(actions, UserView.class);

        assertEquals(stubUserView(), oldUser);

        UserUpdateRequest request = new UserUpdateRequest("Elena", false, "Elena@test.ru",
                Collections.singletonList(Role.ROLE_ADMIN), "test123");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isNoContent());

        actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        UserView userUpdated = TestUtils.readFromJson(actions, UserView.class);

        assertEquals(50L, userUpdated.getId());
        assertEquals(request.getEmail(), userUpdated.getEmail());
        assertEquals(request.getEnabled(), userUpdated.getEnabled());
        assertEquals(request.getName(), userUpdated.getName());
        assertEquals(request.getRoles(), userUpdated.getRoles());
    }

    @Rollback
    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> userService.get(50L));
    }

    private UserView stubUserView() {
        UserView userView = new UserView();
        userView.setEmail("ivanov@test.ru");
        userView.setEnabled(true);
        userView.setId(50L);
        userView.setName("Ivanov");
        userView.setRoles(Collections.singletonList(Role.ROLE_USER));

        return userView;
    }
}