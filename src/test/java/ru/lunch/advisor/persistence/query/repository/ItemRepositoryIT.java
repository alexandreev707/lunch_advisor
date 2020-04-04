package ru.lunch.advisor.persistence.query.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.lunch.advisor.persistence.model.ItemModel;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringJUnitConfig(locations = {"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
public class ItemRepositoryIT {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void findAll() {
        assertEquals(12, itemRepository.findAll().size());
    }

    @Test
    public void get() {
        ItemModel itemModel = itemRepository.findById(50L).orElse(null);

        assertNotNull(itemModel);
        assertEquals(Long.valueOf(50), itemModel.getId());
        assertEquals("Crispy Honey Mustard Chicken Salad", itemModel.getName());
        assertEquals(BigDecimal.valueOf(14).setScale(2), itemModel.getPrice());
    }

    @Rollback
    @Test
    public void create() {
        ItemModel itemModelExpected = new ItemModel("Test item", BigDecimal.valueOf(1.1));

        ItemModel itemModelNew = itemRepository.save(new ItemModel("Test item", BigDecimal.valueOf(1.1)));

        assertNotNull(itemModelExpected);
        assertEquals(itemModelExpected.getName(), itemModelNew.getName());
        assertEquals(itemModelExpected.getPrice(), itemModelNew.getPrice());
        assertEquals(13, itemRepository.findAll().size());
    }

    @Rollback
    @Test
    public void remove() {
        assertEquals(12, itemRepository.findAll().size());
        itemRepository.remove(Arrays.asList(59L, 60L, 61L));
        assertEquals(9, itemRepository.findAll().size());
    }

    @Rollback
    @Test
    public void removeRelationsMenu() {
        assertNotNull(itemRepository.findById(55L).orElse(null));

        itemRepository.removeRelationsMenu(55L);
        itemRepository.deleteById(55L);

        assertNull(itemRepository.findById(55L).orElse(null));
    }
}
