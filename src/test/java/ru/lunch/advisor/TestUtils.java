package ru.lunch.advisor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.lunch.advisor.service.dto.UserDTO;
import ru.lunch.advisor.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {

    public static final UserDTO ADMIN = new UserDTO("admin@test.ru", "admin");
    public static final UserDTO USER = new UserDTO("ivanov@test.ru", "ivanov");

    public static void injectField(Object injectable, String fieldName, Object value) {
        try {
            Field field = injectable.getClass().getDeclaredField(fieldName);
            final boolean fieldAccessible = field.isAccessible();
            field.setAccessible(true);
            field.set(injectable, value);
            field.setAccessible(fieldAccessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> ResultMatcher contentJson(T expected, Class<T> clazz) {
        return result -> assertMatch(readFromJsonMvcResult(result, clazz), expected);
    }

    public static <T> ResultMatcher contentJson(List<T> expected, Class<T> clazz) {
        return result -> assertMatch(readListFromJsonMvcResult(result, clazz), expected);
    }

    public static <T> void assertMatch(List<T> actual, List<T> expected) {
        assertTrue(actual.size() == expected.size() && actual.containsAll(expected));
    }

    public static <T> void assertMatch(T actual, T expected) {
        assertEquals(actual, expected);
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action.andReturn()), clazz);
    }

    public static <T> void assertEqualsList(List<T> actual, List<T> expected, BiConsumer<T, T> consumer, String... fields) {
        if (actual.size() == expected.size()) {
            for (int i = 0; i < expected.size(); i++) {
                assertThat(actual.get(i)).isEqualToIgnoringGivenFields(expected.get(i), fields);
                consumer.accept(actual.get(i), expected.get(i));
            }
        }
    }

    public static RequestPostProcessor userHttpBasic(UserDTO user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public static RequestPostProcessor userAuth(UserDTO user) {
        return SecurityMockMvcRequestPostProcessors
                .authentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
    }
}
