package ru.lunch.advisor.web.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
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
import ru.lunch.advisor.service.ReviewService;
import ru.lunch.advisor.service.State;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.web.controller.rest.ReviewRestController;
import ru.lunch.advisor.web.json.JsonUtil;
import ru.lunch.advisor.web.request.ReviewRequest;
import ru.lunch.advisor.web.response.ReviewUserView;
import ru.lunch.advisor.web.response.ReviewView;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {"vote.time=25"})
@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml",
})
@Transactional
class ReviewRestControllerIT {

    private static final String REST_URL = ReviewRestController.REVIEW_URL + "/";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ReviewService reviewService;

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
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 55)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        ReviewView actual = TestUtils.readFromJson(actions, ReviewView.class);
        ReviewView expected = stubReviewView();
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "dateTime");
        assertEquals(expected.getDateTime().toLocalDate(), actual.getDateTime().toLocalDate());
    }

    @Test
    void byUserId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "user/51")
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<ReviewUserView> views = TestUtils.readListFromJsonMvcResult(mvcResult, ReviewUserView.class);
        TestUtils.assertEqualsList(views, stubReviewsUserView(), assertEqualDateTime(), "dateTime");
    }

    @Test
    void byMenuId() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "menu/56")
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        ReviewView actual = TestUtils.readFromJson(actions, ReviewView.class);
        ReviewView expected = stubReviewView();
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "dateTime");
        assertEquals(expected.getDateTime().toLocalDate(), actual.getDateTime().toLocalDate());
    }

    @Test
    void all() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<ReviewView> views = TestUtils.readListFromJsonMvcResult(mvcResult, ReviewView.class);
        assertEquals(6, views.size());
    }

    @Rollback
    @Test
    void create() throws Exception {
        ReviewRequest request = new ReviewRequest(true, 57L);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isCreated());

        ReviewView created = TestUtils.readFromJson(action, ReviewView.class);
        ReviewView expected = new ReviewView(created.getId(), 51L, 57L, LocalDateTime.now(), State.ACTIVE);

        TestUtils.assertMatch(expected, created);
    }

    @Rollback
    @Test
    void createByRestaurant() throws Exception {
        ReviewRequest request = new ReviewRequest(false, null);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "restaurant/51")
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isCreated());

        ReviewView created = TestUtils.readFromJson(action, ReviewView.class);
        ReviewView expected = new ReviewView(created.getId(), 51L, 54L, LocalDateTime.now(), State.DELETED);

        TestUtils.assertMatch(expected, created);
    }

    @Rollback
    @Test
    void update() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 52L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Assertions.assertEquals(State.DELETED, TestUtils.readFromJson(actions, ReviewView.class).getState());

        ReviewRequest request = new ReviewRequest(true, 53L);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + 52L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isNoContent());

        actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 52L)
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        TestUtils.assertMatch(State.ACTIVE, TestUtils.readFromJson(actions, ReviewView.class).getState());
    }

    @Rollback
    @Test
    void updateByRestaurantId() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "52")
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Assertions.assertEquals(State.DELETED, TestUtils.readFromJson(actions, ReviewView.class).getState());

        ReviewRequest request = new ReviewRequest(true, null);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "restaurant/50")
                .with(TestUtils.userAuth(TestUtils.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(request)))
                .andExpect(status().isNoContent());

        actions = mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "52")
                .with(TestUtils.userAuth(TestUtils.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        TestUtils.assertMatch(State.ACTIVE, TestUtils.readFromJson(actions, ReviewView.class).getState());
    }

    @Rollback
    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + 50L)
                .with(TestUtils.userAuth(TestUtils.ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> reviewService.get(50L));
    }

    private BiConsumer<ReviewUserView, ReviewUserView> assertEqualDateTime() {
        return (actual, expected) -> TestUtils.assertMatch(actual.getDateTime().toLocalDate(),
                expected.getDateTime().toLocalDate());
    }

    private ReviewView stubReviewView() {
        ReviewView reviewView = new ReviewView();
        reviewView.setDateTime(LocalDateTime.now().plusDays(1));
        reviewView.setId(55L);
        reviewView.setMenuId(56L);
        reviewView.setUserId(51L);
        reviewView.setState(State.ACTIVE);

        return reviewView;
    }

    private List<ReviewUserView> stubReviewsUserView() {
        ReviewUserView reviewView1 = new ReviewUserView();
        reviewView1.setDateTime(LocalDateTime.now());
        reviewView1.setId(51L);
        reviewView1.setMenu("Lunch");
        reviewView1.setState(State.ACTIVE.name());

        ReviewUserView reviewView2 = new ReviewUserView();
        reviewView2.setDateTime(LocalDateTime.now().plusDays(1));
        reviewView2.setId(54L);
        reviewView2.setMenu("Lunch");
        reviewView2.setState(State.ACTIVE.name());

        return Arrays.asList(reviewView1, reviewView2);
    }
}
