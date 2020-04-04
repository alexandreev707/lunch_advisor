package ru.lunch.advisor.persistence.query.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.lunch.advisor.persistence.model.RestaurantModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
public class RestaurantRepositoryIT {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    public void findAll() {
        assertEquals(3, restaurantRepository.findAll().size());
    }

    @Test
    public void get() {
        RestaurantModel restaurantModel = restaurantRepository.findById(50L).orElse(null);

        assertNotNull(restaurantModel);
        assertEquals(Long.valueOf(50), restaurantModel.getId());
        assertEquals("SUNRISE", restaurantModel.getName());
        assertEquals("st.Victory 14", restaurantModel.getAddress());
    }


   @Test
    public void getByName() {
        RestaurantModel restaurantModel = restaurantRepository.getByName("SUNRISE").orElse(null);

        assertNotNull(restaurantModel);
        assertEquals(Long.valueOf(50), restaurantModel.getId());
        assertEquals("SUNRISE", restaurantModel.getName());
        assertEquals("st.Victory 14", restaurantModel.getAddress());
    }

    @Rollback
    @Test
    public void create() {
        RestaurantModel restaurantModelExpected = new RestaurantModel();
        restaurantModelExpected.setAddress("Address test");
        restaurantModelExpected.setName("Test restaurant");

        RestaurantModel restaurantModelNew = restaurantRepository.save(restaurantModelExpected);

        assertNotNull(restaurantModelExpected);
        assertEquals(restaurantModelExpected.getName(), restaurantModelNew.getName());
        assertEquals(restaurantModelExpected.getAddress(), restaurantModelNew.getAddress());
    }

    @Rollback
    @Test
    public void remove() {
        assertEquals(3, restaurantRepository.findAll().size());
        restaurantRepository.deleteById(50L);
        assertEquals(2, restaurantRepository.findAll().size());
    }
}
