package ru.lunch.advisor.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.persistence.model.RestaurantModel;
import ru.lunch.advisor.service.dto.RestaurantDTO;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RestaurantMapperTest {

    private RestaurantMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(RestaurantMapper.class);
    }

    @Test
    public void map() {
        RestaurantModel model = stubRestaurantModel();
        RestaurantDTO dto = mapper.map(model);

        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getName(), dto.getName());
        assertEquals(model.getAddress(), dto.getAddress());
    }

    @Test
    public void mapToMenus() {
        RestaurantModel model = stubRestaurantModel();
        List<RestaurantDTO> dto = mapper.map(Collections.singletonList(model));

        assertNotNull(dto);
        assertEquals(model.getId(),dto.get(0).getId());
        assertEquals(model.getName(), dto.get(0).getName());
        assertEquals(model.getAddress(), dto.get(0).getAddress());
    }

    private RestaurantModel stubRestaurantModel() {
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setId(75L);
        restaurantModel.setAddress( "st. Che");
        restaurantModel.setName("EL");

        return restaurantModel;
    }
}
