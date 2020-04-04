package ru.lunch.advisor.web.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.service.dto.ItemDTO;
import ru.lunch.advisor.web.request.ItemRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemWebMapperTest {

    private ItemWebMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(ItemWebMapper.class);
    }

    @Test
    public void map() {
        ItemRequest request = new ItemRequest(50L, "name", BigDecimal.TEN);
        ItemDTO dto = mapper.map(request);

        assertEquals("name", dto.getName());
        assertEquals(BigDecimal.TEN, dto.getPrice());
        assertEquals(50L, dto.getId());
    }

    @Test
    public void mapToList() {
        ItemRequest request = new ItemRequest(50L, "name", BigDecimal.TEN);
        List<ItemDTO> items = mapper.map(Collections.singletonList(request));

        assertNotNull(items);
        assertEquals("name", items.get(0).getName());
        assertEquals(BigDecimal.TEN, items.get(0).getPrice());
        assertEquals(50L, items.get(0).getId());
    }
}
