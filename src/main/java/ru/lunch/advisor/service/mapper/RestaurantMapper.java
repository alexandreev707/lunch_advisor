package ru.lunch.advisor.service.mapper;

import org.mapstruct.Mapper;
import ru.lunch.advisor.persistence.model.RestaurantModel;
import ru.lunch.advisor.service.dto.RestaurantDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDTO map(RestaurantModel restaurantModel);

    List<RestaurantDTO> map(List<RestaurantModel> restaurantModel);
}
