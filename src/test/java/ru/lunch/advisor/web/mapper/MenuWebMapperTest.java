package ru.lunch.advisor.web.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.service.dto.MenuFullDTO;
import ru.lunch.advisor.service.dto.MenuItemDTO;
import ru.lunch.advisor.web.request.ItemRequest;
import ru.lunch.advisor.web.request.MenuCreateRequest;
import ru.lunch.advisor.web.request.MenuUpdateRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MenuWebMapperTest {

    private MenuWebMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(MenuWebMapper.class);
    }

    @Test
    public void map() {
        MenuUpdateRequest request = new MenuUpdateRequest("Lunch", LocalDate.now(), "Restaurant",
                Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN)));
        MenuFullDTO dto = mapper.map(request);

        assertEquals(request.getName(), dto.getName());
        assertEquals(request.getDate(), dto.getDate());
        assertEquals(request.getItems().size(), dto.getItems().size());
        assertEquals(request.getItems().get(0).getName(), dto.getItems().get(0).getName());
        assertEquals(request.getRestaurant(), dto.getRestaurant());
    }

    @Test
    public void mapToMenuItem() {
        MenuUpdateRequest updateRequest = new MenuUpdateRequest("Lunch created", LocalDate.now().plusDays(1),
                "SUNRISE", Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN)));
        MenuItemDTO dto = mapper.mapToMenuItem(updateRequest);

        assertNotNull(dto);
        assertEquals(updateRequest.getRestaurant(), dto.getRestaurant());
        assertEquals(updateRequest.getName(), dto.getName());
        assertEquals(updateRequest.getDate(), dto.getDate());
        assertEquals(updateRequest.getItems().size(), dto.getItems().size());
        assertEquals(updateRequest.getItems().get(0).getName(), dto.getItems().get(0).getName());
        assertEquals(updateRequest.getRestaurant(), dto.getRestaurant());

        MenuCreateRequest createRequest = new MenuCreateRequest("Lunch created", LocalDate.now().plusDays(1),
                "SUNRISE", Collections.singletonList(new ItemRequest("item_name", BigDecimal.TEN)));

        assertNotNull(dto);
        assertEquals(updateRequest.getRestaurant(), dto.getRestaurant());
        assertEquals(updateRequest.getName(), dto.getName());
        assertEquals(createRequest.getDate(), dto.getDate());
        assertEquals(createRequest.getItems().size(), dto.getItems().size());
        assertEquals(createRequest.getItems().get(0).getName(), dto.getItems().get(0).getName());
        assertEquals(createRequest.getRestaurant(), dto.getRestaurant());
    }
}
