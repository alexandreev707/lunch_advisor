package ru.lunch.advisor.web.mapper;

import org.mapstruct.Mapper;
import ru.lunch.advisor.service.dto.ItemDTO;
import ru.lunch.advisor.web.request.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemWebMapper {

    ItemDTO map(ItemRequest request);

    List<ItemDTO> map(List<ItemRequest> request);
}
