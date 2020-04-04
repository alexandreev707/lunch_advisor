package ru.lunch.advisor.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.service.dto.ReviewDTO;
import ru.lunch.advisor.service.dto.ReviewUserDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "menu.id", target = "menuId")
    ReviewDTO map(ReviewModel reviewModel);

    List<ReviewDTO> map(List<ReviewModel> reviewModel);

    @Mapping(source = "menu.name", target = "menu")
    @Mapping(source = "menu.id", target = "menuId")
    ReviewUserDTO mapToUser(ReviewModel reviewModel);

    List<ReviewUserDTO> mapToUser(List<ReviewModel> reviewModel);
}
