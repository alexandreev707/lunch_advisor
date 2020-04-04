package ru.lunch.advisor.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.persistence.model.ItemModel;
import ru.lunch.advisor.service.dto.ItemDTO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemMapperTest {

    private ItemMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(ItemMapper.class);
    }

    @Test
    public void map() {
        ItemModel model = stubIteModel();
        ItemDTO dto = mapper.map(model);

        assertEquals(model.getName(), dto.getName());
        assertEquals(model.getPrice(), dto.getPrice());
    }

    @Test
    public void mapToSet() {
        ItemModel model = stubIteModel();
        Set<ItemDTO> items = mapper.map(Collections.singletonList(model));

        assertNotNull(items);
        items.forEach(i -> {
            assertEquals(model.getId(), i.getId());
            assertEquals(model.getName(), i.getName());
            assertEquals(model.getPrice(), i.getPrice());
        });

        items = mapper.map(new HashSet<>(Collections.singletonList(model)));

        assertNotNull(items);
        items.forEach(i -> {
            assertEquals(model.getId(), i.getId());
            assertEquals(model.getName(), i.getName());
            assertEquals(model.getPrice(), i.getPrice());
        });
    }

    private ItemModel stubIteModel() {
        ItemModel itemModel = new ItemModel();
        itemModel.setId(79L);
        itemModel.setPrice(BigDecimal.TEN);
        itemModel.setName("Кофе");

        return itemModel;
    }
}
