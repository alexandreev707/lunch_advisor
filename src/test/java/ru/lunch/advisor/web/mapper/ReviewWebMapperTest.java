package ru.lunch.advisor.web.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.service.dto.ReviewUpdateDTO;
import ru.lunch.advisor.web.request.ReviewRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewWebMapperTest {

    private ReviewWebMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(ReviewWebMapper.class);
    }

    @Test
    public void map() {
        ReviewRequest request = new ReviewRequest(true, 55L);
        ReviewUpdateDTO dto = mapper.map(request);

        assertEquals(55L, dto.getMenuId());
        assertEquals(true, dto.getVote());
    }
}
