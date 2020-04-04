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
import ru.lunch.advisor.service.MenuService;
import ru.lunch.advisor.service.State;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.web.controller.rest.MenuRestController;
import ru.lunch.advisor.web.json.JsonUtil;
import ru.lunch.advisor.web.request.MenuUpdateRequest;
import ru.lunch.advisor.web.response.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

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
class MenuRestControllerIT {

    private static final String REST_URL = MenuRestController.MENU_URL + "/";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MenuService itemService;

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
                .andExpect(contentJson(stubMenuView(), MenuView.class));
    }

    @Test
    void all() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<MenuReviewView> views = readListFromJsonMvcResult(mvcResult, MenuReviewView.class);
        assertEqualsList(views, stubMenuReviewView(), assertEqualReview(), "reviews");
    }

    @Test
    void byDate() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byDate")
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<MenuItemView> views = readListFromJsonMvcResult(mvcResult, MenuItemView.class);
        assertEqualsList(views, stubMenuItemView(), assertEqualItems(), "items");
    }

    @Rollback
    @Test
    void create() throws Exception {
        MenuUpdateRequest request = new MenuUpdateRequest("Lunch created", LocalDate.now().plusDays(1), "SUNRISE",
                null);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isCreated());

        MenuView created = readFromJson(action, MenuView.class);
        MenuView expected = new MenuView(created.getId(), "Lunch created", LocalDate.now().plusDays(1),
                "SUNRISE");

        assertMatch(created, expected);
    }

    @Rollback
    @Test
    void update() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk());
        MenuView oldItem = readFromJson(response, MenuView.class);

        assertEquals(stubMenuView(), oldItem);

        MenuUpdateRequest request = new MenuUpdateRequest("Lunch updated", LocalDate.now().plusDays(1), "SUNRISE",
                null);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isNoContent());

        response = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk());
        MenuView updatedItem = readFromJson(response, MenuView.class);
        MenuView expectedView = new MenuView(updatedItem.getId(), "Lunch updated", LocalDate.now().plusDays(1),
                "SUNRISE");

        assertMatch(expectedView, updatedItem);
    }

    @Rollback
    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> itemService.get(50L));
    }

    private List<MenuReviewView> stubMenuReviewView() {
        MenuReviewView fullView1 = new MenuReviewView();
        fullView1.setId(53L);
        fullView1.setCountVote(0L);
        fullView1.setDate(LocalDate.now());
        fullView1.setMenu("Lunch");
        fullView1.setRestaurant("SUNRISE");
        fullView1.setReviews(Collections.emptyList());

        MenuReviewView fullView2 = new MenuReviewView();
        fullView2.setId(54L);
        fullView2.setCountVote(2L);
        fullView2.setDate(LocalDate.now());
        fullView2.setMenu("Lunch");
        fullView2.setRestaurant("SKY");
        fullView2.setReviews(Arrays.asList(new ReviewView(53L, 53L, 54L, LocalDateTime.now(), State.ACTIVE),
                new ReviewView(51L, 52L, 54L, LocalDateTime.now(), State.ACTIVE)));

        MenuReviewView fullView3 = new MenuReviewView();
        fullView3.setId(55L);
        fullView3.setCountVote(1L);
        fullView3.setDate(LocalDate.now());
        fullView3.setMenu("Lunch");
        fullView3.setRestaurant("SEA");
        fullView3.setReviews(Collections.singletonList(new ReviewView(50L, 50L, 55L,
                LocalDateTime.now().plusDays(1), State.ACTIVE)));

        return Arrays.asList(fullView1, fullView2, fullView3);
    }

    private List<MenuItemView> stubMenuItemView() {
        MenuItemView itemView1 = new MenuItemView();
        itemView1.setId(53L);
        itemView1.setDate(LocalDate.now());
        itemView1.setItems(Arrays.asList(new ItemView(55L, "COFFEE", BigDecimal.valueOf(7)
                        .setScale(2, RoundingMode.CEILING)),
                new ItemView(54L, "Mashed Potatoes", BigDecimal.valueOf(3)
                        .setScale(2, RoundingMode.CEILING)),
                new ItemView(53L, "Meatloaf", BigDecimal.valueOf(10)
                        .setScale(2, RoundingMode.CEILING))));
        itemView1.setMenu("Lunch");
        itemView1.setRestaurant("SUNRISE");

        MenuItemView itemView2 = new MenuItemView();
        itemView2.setId(54L);
        itemView2.setDate(LocalDate.now());
        itemView2.setItems(Arrays.asList(new ItemView(58L, "Juice", BigDecimal.valueOf(4)
                        .setScale(2, RoundingMode.CEILING)),
                new ItemView(57L, "Shrimps", BigDecimal.valueOf(8)
                        .setScale(2, RoundingMode.CEILING)),
                new ItemView(56L, "Zen Soup", BigDecimal.valueOf(4)
                        .setScale(2, RoundingMode.CEILING))));
        itemView2.setMenu("Lunch");
        itemView2.setRestaurant("SKY");

        MenuItemView itemView3 = new MenuItemView();
        itemView3.setId(55L);
        itemView3.setDate(LocalDate.now());
        itemView3.setItems(Arrays.asList(new ItemView(51L, "Soup and House Salad", BigDecimal.valueOf(5)
                        .setScale(2, RoundingMode.CEILING)),
                new ItemView(50L, "Crispy Honey Mustard Chicken Salad", BigDecimal.valueOf(14)
                        .setScale(2, RoundingMode.CEILING)),
                new ItemView(52L, "TEA", BigDecimal.valueOf(5)
                        .setScale(2, RoundingMode.CEILING))));
        itemView3.setMenu("Lunch");
        itemView3.setRestaurant("SEA");

        return Arrays.asList(itemView1, itemView2, itemView3);
    }

    private MenuView stubMenuView() {
        MenuView menuView = new MenuView();
        menuView.setId(50L);
        menuView.setDate(LocalDate.now().minusDays(1));
        menuView.setMenu("Lunch");
        menuView.setRestaurant("SUNRISE");

        return menuView;
    }

    private BiConsumer<MenuItemView, MenuItemView> assertEqualItems() {
        return (actual, expected) -> assertMatch(actual.getItems(), expected.getItems());
    }

    private BiConsumer<MenuReviewView, MenuReviewView> assertEqualReview() {
        return (actual, expected) -> assertMatch(actual.getReviews(), expected.getReviews());
    }
}