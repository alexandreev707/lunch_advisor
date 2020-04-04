package ru.lunch.advisor.web.validaton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.web.request.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для првоерки валидации запросов, модуль web
 */
public class RequestValidationTest {

    private Validator validator;

    @BeforeEach
    public void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public static Object[][] itemRequestData() {
        return new Object[][]{
                {new ItemRequest("item_name", BigDecimal.TEN), 0},
                {new ItemRequest("", BigDecimal.TEN), 1},
                {new ItemRequest(" ", BigDecimal.TEN), 1},
                {new ItemRequest(" ", BigDecimal.valueOf(-1)), 2},
                {new ItemRequest(null, null), 2}
        };
    }

    @ParameterizedTest
    @MethodSource("itemRequestData")
    public void itemRequest(ItemRequest request, int expected) {
        assertEquals(expected, validator.validate(request).size());
    }

    public static Object[][] menuCreateRequestData() {
        return new Object[][]{
                {new MenuCreateRequest("Lunch", LocalDate.now(), "Restaurant",
                        Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN))), 0},
                {new MenuCreateRequest("Lunch", LocalDate.now(), "Restaurant", null), 0},
                {new MenuCreateRequest(" ", null, " ",
                        Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN))), 3},
                {new MenuCreateRequest("", null, "",
                        Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN))), 3},
                {new MenuCreateRequest(null, null, null,
                        Collections.singletonList(new ItemRequest(" ", null))), 5}
        };
    }

    @ParameterizedTest
    @MethodSource("menuCreateRequestData")
    public void menuCreateRequest(MenuCreateRequest request, int expected) {
        assertEquals(expected, validator.validate(request).size());
    }

    public static Object[][] menuUpdateRequestData() {
        return new Object[][]{
                {new MenuUpdateRequest("Lunch", LocalDate.now(), "Restaurant",
                        Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN))), 0},
                {new MenuUpdateRequest("Lunch", LocalDate.now(), "Restaurant", null), 0},
                {new MenuUpdateRequest(" ", null, " ",
                        Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN))), 1},
                {new MenuUpdateRequest("", null, "",
                        Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN))), 1},
                {new MenuUpdateRequest(null, null, null,
                        Collections.singletonList(new ItemRequest(" ", null))), 3}
        };
    }

    @ParameterizedTest
    @MethodSource("menuUpdateRequestData")
    public void menuUpdateRequest(MenuUpdateRequest request, int expected) {
        assertEquals(expected, validator.validate(request).size());
    }

    public static Object[][] restaurantRequestData() {
        return new Object[][]{
                {new RestaurantRequest("Restaurant", "st.Builder"), 0},
                {new RestaurantRequest("Restaurant", ""), 0},
                {new RestaurantRequest("Restaurant", null), 0},
                {new RestaurantRequest("", null), 1},
                {new RestaurantRequest(" ", null), 1},
                {new RestaurantRequest(null, null), 1}
        };
    }

    @ParameterizedTest
    @MethodSource("restaurantRequestData")
    public void restaurantRequest(RestaurantRequest request, int expected) {
        assertEquals(expected, validator.validate(request).size());
    }

    public static Object[][] reviewRequestData() {
        return new Object[][]{
                {new ReviewRequest(true, 50L), 0},
                {new ReviewRequest(false, 50L), 0},
                {new ReviewRequest(null, 50L), 1},
                {new ReviewRequest(null, -5L), 2},
                {new ReviewRequest(null, null), 2},
        };
    }

    @ParameterizedTest
    @MethodSource("reviewRequestData")
    public void reviewRequest(ReviewRequest request, int expected) {
        assertEquals(expected, validator.validate(request).size());
    }

    public static Object[][] userRequestData() {
        return new Object[][]{
                {new UserUpdateRequest("Тестовый", true, "test@test.ru",
                        Collections.singletonList(Role.ROLE_USER), "Password1"), 0},
                {new UserUpdateRequest("Тестовый", true, "test@test.ru",
                        Collections.singletonList(Role.ROLE_USER), ""), 0},
                {new UserUpdateRequest("Тестовый", true, "test@test.ru",
                        Collections.singletonList(Role.ROLE_USER), " "), 0},
                {new UserUpdateRequest("Тестовый", true, "test@test.ru",
                        Collections.emptyList(), null), 1},
                {new UserUpdateRequest("Тестовый", true, "test@test.ru",
                        null, null), 1},
                {new UserUpdateRequest("Тестовый", true, "testtest.ru",
                        null, null), 2},
                {new UserUpdateRequest("Тестовый", true, "",
                        null, null), 1},
                {new UserUpdateRequest("Тестовый", null, " ",
                        null, null), 3},
                {new UserUpdateRequest("", null, null,
                        null, null), 2},
                {new UserUpdateRequest(" ", null, null,
                        null, null), 2},
                {new UserUpdateRequest(null, null, null,
                        null, null), 2}
        };
    }

    @ParameterizedTest
    @MethodSource("userRequestData")
    public void userRequest(UserUpdateRequest request, int expected) {
        assertEquals(expected, validator.validate(request).size());
    }

    public static Object[][] userCreateRequestData() {
        return new Object[][]{
                {new UserCreateRequest("Тестовый", true, "test@test.ru",
                        Collections.singletonList(Role.ROLE_USER), "Password1"), 0},
                {new UserCreateRequest("Тестовый", true, "test@test.ru",
                        Collections.singletonList(Role.ROLE_USER), ""), 1},
                {new UserCreateRequest("Тестовый", true, "test@test.ru",
                        Collections.singletonList(Role.ROLE_USER), " "), 1},
                {new UserCreateRequest("Тестовый", true, "test@test.ru",
                        Collections.emptyList(), null), 2},
                {new UserCreateRequest("Тестовый", true, "test@test.ru",
                        null, null), 2},
                {new UserCreateRequest("Тестовый", true, "testtest.ru",
                        null, null), 3},
                {new UserCreateRequest("Тестовый", true, "",
                        null, null), 3},
                {new UserCreateRequest("Тестовый", null, " ",
                        null, null), 5},
                {new UserCreateRequest("", null, null,
                        null, null), 5},
                {new UserCreateRequest(" ", null, null,
                        null, null), 5},
                {new UserCreateRequest(null, null, null,
                        null, null), 5}
        };
    }

    @ParameterizedTest
    @MethodSource("userCreateRequestData")
    public void userRequest(UserCreateRequest request, int expected) {
        assertEquals(expected, validator.validate(request).size());
    }
}
