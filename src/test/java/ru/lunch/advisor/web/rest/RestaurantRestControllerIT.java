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
import ru.lunch.advisor.service.RestaurantService;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.web.controller.rest.RestaurantRestController;
import ru.lunch.advisor.web.json.JsonUtil;
import ru.lunch.advisor.web.request.RestaurantRequest;
import ru.lunch.advisor.web.response.RestaurantView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.lunch.advisor.TestUtils.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@Transactional
class RestaurantRestControllerIT {

    private static final String REST_URL = RestaurantRestController.RESTAURANT_URL + "/";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private RestaurantService restaurantService;

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
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(stubRestaurantView(), RestaurantView.class));
    }

    @Test
    void all() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<RestaurantView> views = readListFromJsonMvcResult(mvcResult, RestaurantView.class);
        assertEquals(3, views.size());
    }

    @Rollback
    @Test
    void create() throws Exception {
        RestaurantRequest request = new RestaurantRequest("Tip-top", "Победы");

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isCreated());

        RestaurantView created = readFromJson(action, RestaurantView.class);
        RestaurantView expected = new RestaurantView(created.getId(), "Tip-top", "Победы");

        assertMatch(expected, created);
    }

    @Rollback
    @Test
    void update() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assertEquals(readFromJson(actions, RestaurantView.class), stubRestaurantView());

        RestaurantRequest request = new RestaurantRequest("Clock", "ул.Гагарина 37");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isNoContent());

        actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        RestaurantView restaurant = readFromJson(actions, RestaurantView.class);
        assertEquals("Clock", restaurant.getName());
        assertEquals("ул.Гагарина 37", restaurant.getAddress());
    }

    @Rollback
    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> restaurantService.get(50L));
    }

    private RestaurantView stubRestaurantView() {
        RestaurantView restaurantView = new RestaurantView();
        restaurantView.setAddress("st.Victory 14");
        restaurantView.setId(50L);
        restaurantView.setName("SUNRISE");

        return restaurantView;
    }
}
