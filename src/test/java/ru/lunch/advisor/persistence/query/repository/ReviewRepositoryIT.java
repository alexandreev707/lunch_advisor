package ru.lunch.advisor.persistence.query.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.lunch.advisor.persistence.model.*;
import ru.lunch.advisor.service.State;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
public class ReviewRepositoryIT {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void findAll() {
        assertEquals(6, reviewRepository.findAll().size());
    }

    @Test
    public void get() {
        ReviewModel reviewModel = reviewRepository.findById(50L).orElse(null);

        assertNotNull(reviewModel);
        assertEquals(Long.valueOf(50), reviewModel.getId());
        assertEquals(LocalDateTime.now().toLocalDate(), reviewModel.getDateTime().toLocalDate());
        assertNotNull(reviewModel.getMenu());
        assertNotNull(reviewModel.getUser());
    }

    public static Object[][] getByUserIdAndBetweenData() {
        return new Object[][]{
                {LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().atTime(LocalTime.MAX), 50L, 1},
                {LocalDate.now().minusDays(1).atTime(LocalTime.MIN), LocalDate.now().atTime(LocalTime.MAX), 52L, 2},
                {LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().plusDays(1).atTime(LocalTime.MAX), 51L, 1},
                {LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().plusDays(1).atTime(LocalTime.MAX), 55L, 0},
                {LocalDate.now().plusDays(5).atTime(LocalTime.MIN), LocalDate.now().plusDays(10).atTime(LocalTime.MAX), 51L, 0},
                {LocalDate.now().minusDays(10).atTime(LocalTime.MIN), LocalDate.now().minusDays(1).atTime(LocalTime.MAX), 51L, 0},
                {null, null, null, 0}
        };
    }

    @ParameterizedTest
    @MethodSource("getByUserIdAndBetweenData")
    public void getByUserIdAndBetween(LocalDateTime start, LocalDateTime end, Long id, int expected) {
        List<ReviewModel> reviewModel = reviewRepository.getByUserIdAndBetween(start, end, id);

        assertEquals(expected, reviewModel.size());
        reviewModel.forEach(r -> {
            LocalDate date = r.getDateTime().toLocalDate();
            assertTrue(start.toLocalDate().equals(date) || end.toLocalDate().equals(date));
            Assertions.assertEquals(State.ACTIVE.name(), r.getState().name());
        });
    }

    public static Object[][] getByUserAndMenuData() {
        return new Object[][]{
                {LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().atTime(LocalTime.MAX), 56L, 50L},
                {LocalDate.now().minusDays(1).atTime(LocalTime.MIN), LocalDate.now().atTime(LocalTime.MAX), 50L, 52L},
                {LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().plusDays(1).atTime(LocalTime.MAX), 56L, 51L},
                {LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().plusDays(1).atTime(LocalTime.MAX), 55L, 57L},
                {null, null, null, null}
        };
    }

    @ParameterizedTest
    @MethodSource("getByUserAndMenuData")
    public void getByUserAndMenu(LocalDateTime start, LocalDateTime end, Long menuId, Long userId) {
        ReviewModel reviewModel = reviewRepository.getByUserAndMenu(menuId, userId)
                .orElse(null);

        if (reviewModel == null) {
            assertNull(reviewModel);
        } else {
            assertEquals(menuId, reviewModel.getMenu().getId());
            assertEquals(userId, reviewModel.getUser().getId());
            LocalDate date = reviewModel.getDateTime().toLocalDate();
            assertTrue(start.toLocalDate().equals(date) || end.toLocalDate().equals(date));
        }
    }

    public static Object[][] getByUserIdData() {
        return new Object[][]{
                {50L, 2},
                {51L, 1},
                {52L, 2},
                {null, 0}
        };
    }

    @ParameterizedTest
    @MethodSource("getByUserIdData")
    public void byUserId(Long userId, int expected) {
        List<ReviewModel> reviewModel = reviewRepository.byUserId(userId);
        assertEquals(expected, reviewModel.size());
    }

    @Test
    public void update() {
        ReviewModel reviewModel = reviewRepository.findById(50L).orElse(null);
        assertNotNull(reviewModel);
        assertEquals(Long.valueOf(50), reviewModel.getId());
        Assertions.assertEquals(State.ACTIVE, reviewModel.getState());
        assertEquals(LocalDateTime.now().toLocalDate(), reviewModel.getDateTime().toLocalDate());
        assertNotNull(reviewModel.getMenu());
        assertNotNull(reviewModel.getUser());

        LocalDateTime dateExpected = LocalDateTime.now();
        State stateExpected = State.DELETED;
        reviewModel.setState(stateExpected);
        reviewModel.setDateTime(dateExpected);

        reviewModel = reviewRepository.save(reviewModel);

        assertEquals(Long.valueOf(50), reviewModel.getId());
        Assertions.assertEquals(stateExpected, reviewModel.getState());
        assertEquals(dateExpected, reviewModel.getDateTime());
    }

    @Rollback
    @Test
    public void create() {
        ReviewModel reviewModelExpected = new ReviewModel();
        reviewModelExpected.setUser(stubUserModel());
        reviewModelExpected.setMenu(stubMenuModel());
        reviewModelExpected.setDateTime(LocalDateTime.now());

        ReviewModel reviewModelNew = reviewRepository.save(reviewModelExpected);

        assertNotNull(reviewModelExpected);
        assertEquals(reviewModelExpected.getDateTime(), reviewModelNew.getDateTime());
        assertEquals(reviewModelExpected.getUser(), reviewModelNew.getUser());
        assertEquals(reviewModelExpected.getMenu(), reviewModelNew.getMenu());
    }

    @Rollback
    @Test
    public void remove() {
        assertEquals(6, reviewRepository.findAll().size());
        reviewRepository.deleteById(50L);
        assertEquals(5, reviewRepository.findAll().size());
    }

    @Rollback
    @Test
    public void deleteVote() {
        List<ReviewModel> reviewModels = reviewRepository.findAll().stream().filter(r -> State.ACTIVE.equals(r.getState()))
                .collect(Collectors.toList());
        reviewModels.forEach(r -> Assertions.assertEquals(State.ACTIVE.name(), r.getState().name()));

        entityManager.clear();
        reviewRepository.deleteVote(reviewModels.stream()
                .map(ReviewModel::getId)
                .collect(Collectors.toList()));

        reviewRepository.findAll().forEach(r -> Assertions.assertEquals(State.DELETED.name(), r.getState().name()));
    }

    @Rollback
    @Test
    public void removeByUserId() {
        assertEquals(6, reviewRepository.findAll().size());
        reviewRepository.removeByUserId(50L);
        assertEquals(4, reviewRepository.findAll().size());
    }

    private UserModel stubUserModel() {
        UserModel userModel = new UserModel();
        userModel.setEmail("user_test@test.com");
        userModel.setName("User_test");
        userModel.setPassword("Test pass7");
        userModel.setEnabled(true);
        userModel.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_ADMIN, Role.ROLE_USER)));

        return userModel;
    }

    private MenuModel stubMenuModel() {
        MenuModel menuModel = new MenuModel();
        menuModel.setId(45L);
        menuModel.setDate(LocalDate.now());
        menuModel.setName("Test");
        menuModel.setItems(new HashSet<>(Collections.singletonList(new ItemModel("Lunch", BigDecimal.TEN))));
        menuModel.setRestaurant(new RestaurantModel("EL", "st. Che"));
        menuModel.setReviews(new HashSet<>(Collections.singletonList(new ReviewModel())));

        return menuModel;
    }
}
