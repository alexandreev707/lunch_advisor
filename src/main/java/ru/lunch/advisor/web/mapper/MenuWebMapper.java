package ru.lunch.advisor.web.mapper;

import org.mapstruct.Mapper;
import ru.lunch.advisor.service.dto.MenuFullDTO;
import ru.lunch.advisor.service.dto.MenuItemDTO;
import ru.lunch.advisor.web.request.MenuCreateRequest;
import ru.lunch.advisor.web.request.MenuUpdateRequest;

@Mapper(componentModel = "spring")
public interface MenuWebMapper {

    MenuFullDTO map(MenuUpdateRequest request);

    MenuItemDTO mapToMenuItem(MenuUpdateRequest request);

    MenuItemDTO mapToMenuItem(MenuCreateRequest request);
}
