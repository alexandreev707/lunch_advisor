package ru.lunch.advisor.persistence.query.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.lunch.advisor.persistence.model.ItemModel;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.model.RestaurantModel;
import ru.lunch.advisor.persistence.model.ReviewModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
public class MenuRepositoryIT {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    public void allWithReviews() {
        List<MenuModel> menuModels = menuRepository.allWithReviews();

        assertNotNull(menuModels);
        assertEquals(8, menuModels.size());
        menuModels.forEach(m -> {
            assertTrue(m.getItems().size() >= 0);
        });
    }

    @Test
    public void eqDateWithReviewItems() {
        List<MenuModel> menuModels = menuRepository.eqDateWithReviewItems(LocalDate.now());

        assertNotNull(menuModels);
        menuModels.forEach(m -> {
            assertTrue(m.getItems().size() > 0);
            assertTrue(m.getReviews().size() > 0);
        });
    }

    @Test
    public void byDateAndRestaurant() {
        MenuModel menuModel = menuRepository.byDateAndRestaurant(LocalDate.now(), 51L).orElse(null);

        assertNotNull(menuModel);
        assertEquals(54L, menuModel.getId());
        assertEquals(LocalDate.now(), menuModel.getDate());
        assertEquals("SKY", menuModel.getRestaurant().getName());
        assertEquals(3, menuModel.getItems().size());
        assertEquals(2, menuModel.getReviews().size());
    }

    @Test
    public void byIdWithItems() {
        MenuModel menuModel = menuRepository.byIdWithItems(50L).orElse(null);

        assertNotNull(menuModel);
        assertEquals(50L, menuModel.getId());
        assertEquals("Lunch", menuModel.getName());
    }

    @Test
    public void get() {
        MenuModel menuModel = menuRepository.findById(53L).orElse(null);

        assertNotNull(menuModel);
        assertEquals(53L, menuModel.getId());
        assertEquals(LocalDate.now(), menuModel.getDate());
        assertNotNull(menuModel.getItems());
        assertEquals("Lunch", menuModel.getName());
        assertNotNull(menuModel.getReviews());
        assertNotNull(menuModel.getRestaurant());
    }

    @Rollback
    @Test
    public void create() {
        MenuModel menuModel = menuRepository.save(stubMenuModel());

        assertNotNull(menuModel);
        assertEquals("Test", menuModel.getName());
        assertEquals(LocalDate.now(), menuModel.getDate());
        assertNotNull(menuModel.getRestaurant());
        assertNotNull(menuModel.getItems());
    }

    @Rollback
    @Test
    public void remove() {
        assertEquals(8, menuRepository.findAll().size());
        menuRepository.deleteById(53L);
        assertEquals(7, menuRepository.findAll().size());
    }

    private MenuModel stubMenuModel() {
        MenuModel menuModel = new MenuModel();
        menuModel.setId(45L);
        menuModel.setDate(LocalDate.now());
        menuModel.setName("Test");
        menuModel.setItems(new HashSet<>(Collections.singletonList(new ItemModel("Lunch", BigDecimal.TEN))));
        menuModel.setRestaurant( new RestaurantModel("EL", "st. Che"));
        menuModel.setReviews(new HashSet<>(Collections.singletonList(new ReviewModel())));

        return menuModel;
    }
}
