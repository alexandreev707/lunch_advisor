package ru.lunch.advisor.web.mapper;

import org.mapstruct.Mapper;
import ru.lunch.advisor.service.dto.ReviewUpdateDTO;
import ru.lunch.advisor.web.request.ReviewRequest;

@Mapper(componentModel = "spring")
public interface ReviewWebMapper {

    ReviewUpdateDTO map(ReviewRequest review);
}
