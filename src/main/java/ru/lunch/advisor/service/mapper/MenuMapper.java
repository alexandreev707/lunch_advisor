package ru.lunch.advisor.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.service.dto.MenuDTO;
import ru.lunch.advisor.service.dto.MenuFullDTO;
import ru.lunch.advisor.service.dto.MenuItemDTO;
import ru.lunch.advisor.service.dto.ReviewDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    @Mapping(source = "restaurant.name", target = "restaurant")
    MenuFullDTO mapToMenuFull(MenuModel menuModel);

    @Mapping(source = "restaurant.name", target = "restaurant")
    MenuItemDTO mapToMenuItem(MenuModel menuModel);

    @Mapping(source = "restaurant.name", target = "restaurant")
    MenuDTO mapToMenu(MenuModel menuModel);

    List<MenuDTO> mapToMenu(List<MenuModel> menuModels);

    List<MenuFullDTO> mapToMenuFull(List<MenuModel> menuModels);

    List<MenuItemDTO> mapToMenuItem(List<MenuModel> menuModels);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "menu.id", target = "menuId")
    ReviewDTO map(ReviewModel reviewModel);
}
