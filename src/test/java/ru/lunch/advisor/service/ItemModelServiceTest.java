package ru.lunch.advisor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.lunch.advisor.persistence.model.ItemModel;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.query.repository.ItemRepository;
import ru.lunch.advisor.persistence.query.repository.MenuRepository;
import ru.lunch.advisor.service.dto.ItemDTO;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.ItemMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ItemModelServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository repository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ItemMapper mapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void get() {
        ItemDTO expected = stubItemDTO();
        Mockito.when(repository.findById(50L)).thenReturn(Optional.of(Mockito.mock(ItemModel.class)));
        Mockito.when(mapper.map(Mockito.any(ItemModel.class))).thenReturn(expected);

        ItemDTO dto = itemService.get(50L);
        assertEquals(expected.getName(), dto.getName());
        assertEquals(expected.getPrice(), dto.getPrice());
        assertEquals(expected.getId(), dto.getId());

        Mockito.verify(repository, Mockito.times(1)).findById(50L);
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.any(ItemModel.class));
    }

    @Test
    public void getException() {
        assertThrows(NotFoundException.class, () -> itemService.get(50L));
    }

    @Test
    public void all() {
        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(Mockito.mock(ItemModel.class)));
        Mockito.when(mapper.map(Mockito.anyList())).thenReturn(new HashSet<>(Collections.singletonList(stubItemDTO())));

        mapper.map(repository.findAll());

        Mockito.verify(repository, Mockito.times(1)).findAll();
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.anyList());
    }

    @Test
    public void getItemsByMenu() {
        MenuModel menuModel = Mockito.mock(MenuModel.class);
        Mockito.when(menuModel.getItems()).thenReturn(new HashSet<>(Collections.singletonList(new ItemModel())));
        Mockito.when(menuRepository.byIdWithItems(55L)).thenReturn(Optional.of(menuModel));
        Mockito.when(mapper.map(Mockito.anyList())).thenReturn(new HashSet<>(Collections.singletonList(stubItemDTO())));

        Set<ItemDTO> items = itemService.getItemsByMenu(55L);

        assertNotNull(items);
        Mockito.verify(menuRepository, Mockito.times(1)).byIdWithItems(55L);
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.anySet());
    }

    @Test
    public void getItemsByMenuException() {
        assertThrows(NotFoundException.class, () -> itemService.getItemsByMenu(55L));
    }

    @Test
    public void create() {
        itemService.create(new ItemDTO());
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(ItemModel.class));
    }

    @Test
    public void update() {
        Mockito.when(repository.findById(50L)).thenReturn(Optional.of(new ItemModel()));

        itemService.update(50L, new ItemDTO());

        Mockito.verify(repository, Mockito.times(1)).findById(50L);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(ItemModel.class));
    }

    @Test
    public void updateException() {
        assertThrows(NotFoundException.class, () -> itemService.update(50L, new ItemDTO()));
    }


    @Test
    public void remove() {
        itemService.remove(50L);
        Mockito.verify(repository, Mockito.times(1)).deleteById(50L);

    }

    private ItemDTO stubItemDTO() {
        ItemDTO dto = new ItemDTO();
        dto.setPrice(BigDecimal.TEN);
        dto.setName("Coffee");
        dto.setId(43L);

        return dto;
    }
}
