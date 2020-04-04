package ru.lunch.advisor.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.lunch.advisor.persistence.model.ItemModel;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.model.RestaurantModel;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.service.dto.MenuDTO;
import ru.lunch.advisor.service.dto.MenuFullDTO;
import ru.lunch.advisor.service.dto.MenuItemDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MenuMapperTest {

    private MenuMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(MenuMapper.class);
    }

    @Test
    public void mapToMenuFull() {
        MenuModel model = stubMenuModel();
        MenuFullDTO dto = mapper.mapToMenuFull(model);

        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getName(), dto.getName());
        assertEquals(model.getDate(), dto.getDate());
        assertEquals(1, dto.getItems().size());
        assertEquals(1, dto.getReviews().size());
        assertEquals("EL", dto.getRestaurant());
    }

    @Test
    public void mapToMenusFull() {
        MenuModel model = stubMenuModel();
        List<MenuFullDTO> dto = mapper.mapToMenuFull(Collections.singletonList(model));

        assertNotNull(dto);
        assertEquals(model.getId(), dto.get(0).getId());
        assertEquals(model.getName(), dto.get(0).getName());
        assertEquals(model.getDate(), dto.get(0).getDate());
        assertEquals(1, dto.get(0).getItems().size());
        assertEquals(1, dto.get(0).getReviews().size());
        assertEquals("EL", dto.get(0).getRestaurant());
    }

    @Test
    public void mapToMenuItem() {
        MenuModel model = stubMenuModel();
        MenuItemDTO dto = mapper.mapToMenuItem(model);

        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getName(), dto.getName());
        assertEquals(model.getDate(), dto.getDate());
        assertEquals(1, dto.getItems().size());
        assertEquals("EL", dto.getRestaurant());
    }

    @Test
    public void mapToMenusItem() {
        MenuModel model = stubMenuModel();
        List<MenuItemDTO> dto = mapper.mapToMenuItem(Collections.singletonList(model));

        assertNotNull(dto);
        assertEquals(model.getId(), dto.get(0).getId());
        assertEquals(model.getName(), dto.get(0).getName());
        assertEquals(model.getDate(), dto.get(0).getDate());
        assertEquals(1, dto.get(0).getItems().size());
        assertEquals("EL", dto.get(0).getRestaurant());
    }

    @Test
    public void mapToMenu() {
        MenuModel model = stubMenuModel();
        MenuDTO dto = mapper.mapToMenu(model);

        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getName(), dto.getName());
        assertEquals(model.getDate(), dto.getDate());
        assertEquals("EL", dto.getRestaurant());
    }

    @Test
    public void mapToMenus() {
        MenuModel model = stubMenuModel();
        List<MenuDTO> dto = mapper.mapToMenu(Collections.singletonList(model));

        assertNotNull(dto);
        assertEquals(model.getId(), dto.get(0).getId());
        assertEquals(model.getName(), dto.get(0).getName());
        assertEquals(model.getDate(), dto.get(0).getDate());
        assertEquals("EL", dto.get(0).getRestaurant());
    }

    private MenuModel stubMenuModel() {
        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setAddress("st. Che");
        restaurantModel.setName("EL");

        MenuModel menuModel = new MenuModel();
        menuModel.setId(45L);
        menuModel.setDate(LocalDate.now());
        menuModel.setName("Test");
        menuModel.setItems(new HashSet<>(Collections.singletonList(new ItemModel("Lunch", BigDecimal.TEN))));
        menuModel.setRestaurant(restaurantModel);
        menuModel.setReviews(new HashSet<>(Collections.singletonList(new ReviewModel())));

        return menuModel;
    }
}
