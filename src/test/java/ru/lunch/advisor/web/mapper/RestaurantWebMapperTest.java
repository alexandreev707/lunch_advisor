package ru.lunch.advisor.web.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.service.dto.RestaurantDTO;
import ru.lunch.advisor.web.request.RestaurantRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestaurantWebMapperTest {

    private RestaurantWebMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(RestaurantWebMapper.class);
    }

    @Test
    public void map() {
        RestaurantRequest request = new RestaurantRequest("SUNRISE", "st.Victory");
        RestaurantDTO dto = mapper.map(request);

        assertEquals("SUNRISE", dto.getName());
        assertEquals("st.Victory", dto.getAddress());
    }
}
