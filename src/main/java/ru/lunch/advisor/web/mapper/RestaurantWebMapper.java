package ru.lunch.advisor.web.mapper;

import org.mapstruct.Mapper;
import ru.lunch.advisor.service.dto.RestaurantDTO;
import ru.lunch.advisor.web.request.RestaurantRequest;

@Mapper(componentModel = "spring")
public interface RestaurantWebMapper {

    RestaurantDTO map(RestaurantRequest restaurant);
}
