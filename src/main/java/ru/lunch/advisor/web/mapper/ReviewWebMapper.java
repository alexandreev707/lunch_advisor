package ru.lunch.advisor.web.mapper;

import org.mapstruct.Mapper;
import ru.lunch.advisor.service.dto.ReviewUpdateDTO;
import ru.lunch.advisor.web.request.ReviewRequest;
import ru.lunch.advisor.web.request.ReviewRestaurantRequest;

@Mapper(componentModel = "spring")
public interface ReviewWebMapper {

    ReviewUpdateDTO map(ReviewRequest review);

    ReviewUpdateDTO map(ReviewRestaurantRequest review);
}
