package ru.lunch.advisor.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.persistence.model.Role;
import ru.lunch.advisor.persistence.model.UserModel;
import ru.lunch.advisor.service.State;
import ru.lunch.advisor.service.dto.ReviewDTO;
import ru.lunch.advisor.service.dto.ReviewUserDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReviewMapperTest {

    private ReviewMapper mapper;

    private static final LocalDateTime DATE_TIME = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(ReviewMapper.class);
    }

    @Test
    public void map() {
        ReviewModel model = stubReviewModel();
        ReviewDTO dto = mapper.map(model);

        assertEquals(model.getId(), dto.getId());
        assertEquals(70L, dto.getMenuId());
        assertEquals(88L, dto.getUserId());
        assertEquals(DATE_TIME, dto.getDateTime());
        assertEquals(State.DELETED, dto.getState());
    }

    @Test
    public void mapToReviews() {
        ReviewModel model = stubReviewModel();
        List<ReviewDTO> dto = mapper.map(Collections.singletonList(model));

        assertNotNull(dto);
        assertEquals(model.getId(), dto.get(0).getId());
        assertEquals(70L, dto.get(0).getMenuId());
        assertEquals(88L, dto.get(0).getUserId());
        assertEquals(DATE_TIME, dto.get(0).getDateTime());
        assertEquals(State.DELETED, dto.get(0).getState());
    }

    @Test
    public void mapToUser() {
        ReviewModel model = stubReviewModel();
        ReviewUserDTO dto = mapper.mapToUser(model);

        assertEquals(model.getId(), dto.getId());
        assertEquals(70L, dto.getMenuId());
        assertEquals("Lunch", dto.getMenu());
        assertEquals(DATE_TIME, dto.getDateTime());
        assertEquals(State.DELETED, dto.getState());
    }

    @Test
    public void mapToUsers() {
        ReviewModel model = stubReviewModel();
        List<ReviewUserDTO> dto = mapper.mapToUser(Collections.singletonList(model));

        assertNotNull(dto);
        assertEquals(model.getId(), dto.get(0).getId());
        assertEquals(70L, dto.get(0).getMenuId());
        assertEquals("Lunch", dto.get(0).getMenu());
        assertEquals(DATE_TIME, dto.get(0).getDateTime());
        assertEquals(State.DELETED, dto.get(0).getState());
    }

    private ReviewModel stubReviewModel() {
        MenuModel menuModel = new MenuModel();
        menuModel.setId(70L);
        menuModel.setName("Lunch");

        UserModel userModel = new UserModel();
        userModel.setId(88L);
        userModel.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)));

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setId(65L);
        reviewModel.setUser(userModel);
        reviewModel.setMenu(menuModel);
        reviewModel.setDateTime(DATE_TIME);
        reviewModel.setState(State.DELETED);

        return reviewModel;
    }
}
