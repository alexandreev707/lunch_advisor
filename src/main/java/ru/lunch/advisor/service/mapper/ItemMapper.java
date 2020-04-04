package ru.lunch.advisor.service.mapper;

import org.mapstruct.Mapper;
import ru.lunch.advisor.persistence.model.ItemModel;
import ru.lunch.advisor.service.dto.ItemDTO;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDTO map(ItemModel item);

    Set<ItemDTO> map(List<ItemModel> items);

    Set<ItemDTO> map(Set<ItemModel> items);
}
