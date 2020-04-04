package ru.lunch.advisor.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.lunch.advisor.persistence.model.ItemModel;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.model.RestaurantModel;
import ru.lunch.advisor.persistence.query.MenuQuery;
import ru.lunch.advisor.persistence.query.repository.MenuRepository;
import ru.lunch.advisor.persistence.query.repository.RestaurantRepository;
import ru.lunch.advisor.service.dto.ItemDTO;
import ru.lunch.advisor.service.dto.MenuDTO;
import ru.lunch.advisor.service.dto.MenuFullDTO;
import ru.lunch.advisor.service.dto.MenuItemDTO;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.MenuMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository repository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private MenuQuery menuQuery;
    @Mock
    private MenuMapper mapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void create() {
        MenuItemDTO dto = stubMenuItemDTO();
        Mockito.when(restaurantRepository.getByName(dto.getRestaurant()))
                .thenReturn(Optional.of(Mockito.mock(RestaurantModel.class)));

        menuService.create(dto);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(MenuModel.class));
        Mockito.verify(restaurantRepository, Mockito.times(1)).getByName(dto.getRestaurant());
    }

    @Test
    public void createException() {
        MenuItemDTO dto = Mockito.mock(MenuItemDTO.class);
        Mockito.when(dto.getRestaurant()).thenReturn("Restaurant test");

        assertThrows(NotFoundException.class, () -> menuService.create(dto));
    }

    @Test
    public void getAll() {
        Mockito.when(mapper.mapToMenuFull(Mockito.anyList()))
                .thenReturn(Collections.singletonList(new MenuFullDTO()));

        List<MenuFullDTO> dtos = menuService.all();
        assertNotNull(dtos);
        assertEquals(1, menuService.all().size());
    }

    @Test
    public void update() {
        MenuItemDTO dto = stubMenuItemDTO();
        MenuModel menuModel = Mockito.mock(MenuModel.class);
        ItemModel itemModel = new ItemModel();
        itemModel.setId(55L);
        itemModel.setName("Coffee");
        itemModel.setPrice(BigDecimal.TEN);
        Mockito.when(menuModel.getItems()).thenReturn(new HashSet<>(Arrays.asList(itemModel)));
        Mockito.when(repository.findById(50L)).thenReturn(Optional.of(menuModel));

        menuService.update(50L, dto);

        Mockito.verify(menuModel, Mockito.times(1)).setName(dto.getName());
        Mockito.verify(menuModel, Mockito.times(1)).setDate(dto.getDate());
        Mockito.verify(repository, Mockito.times(1)).findById(50L);
        Mockito.verify(repository, Mockito.times(1)).save(menuModel);
    }

    @Test
    public void updateException() {
        Assertions.assertThrows(NotFoundException.class, () -> menuService.update(50L,
                Mockito.mock(MenuItemDTO.class)));
    }

    @Test
    public void get() {
        MenuDTO dto = stubMenuDTO();

        Mockito.when(mapper.mapToMenu(Mockito.any(MenuModel.class))).thenReturn(dto);
        Mockito.when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(Mockito.mock(MenuModel.class)));

        MenuDTO menu = menuService.get(50L);

        assertNotNull(menu);
        assertEquals(dto.getName(), menu.getName());
        assertEquals(dto.getDate(), menu.getDate());
        assertEquals(dto.getRestaurant(), menu.getRestaurant());
        assertEquals(dto.getId(), menu.getId());
    }


    private MenuDTO stubMenuDTO() {
        MenuDTO dto = new MenuDTO();
        dto.setDate(LocalDate.now());
        dto.setId(50L);
        dto.setName("Menu test");
        dto.setRestaurant("Restaurant test");

        return dto;
    }

    private MenuItemDTO stubMenuItemDTO() {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setDate(LocalDate.now());
        dto.setId(50L);

        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setId(4L);
        itemDTO1.setName("Tea");
        itemDTO1.setPrice(BigDecimal.valueOf(55));
        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setId(55L);
        itemDTO2.setName("Coffee");
        itemDTO2.setPrice(BigDecimal.valueOf(15));
        dto.setItems(Collections.singletonList(itemDTO2));

        dto.setName("Menu test");
        dto.setRestaurant("Restaurant test");

        return dto;
    }

    @Test
    public void getException() {
        Assertions.assertThrows(NotFoundException.class, () -> menuService.get(50L));
    }

    @Test
    public void remove() {
        Mockito.when(repository.getOne(50L)).thenReturn(Mockito.mock(MenuModel.class));
        menuService.remove(50L);
        Mockito.verify(repository, Mockito.times(1)).getOne(50L);
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any(MenuModel.class));
    }

    @Test
    public void byDateWithItems() {
        LocalDate date = LocalDate.now();
        Mockito.when(repository.eqDateWithReviewItems(date)).thenReturn(Collections.emptyList());

        menuService.byDateWithItems(date);

        Mockito.verify(mapper, Mockito.times(1)).mapToMenuFull(Mockito.anyList());
        Mockito.verify(repository, Mockito.times(1)).eqDateWithReviewItems(date);
    }

    @Test
    public void byDateWithoutRelations() {
        MenuDTO expected = stubMenuDTO();
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        List<MenuModel> models = Collections.singletonList(Mockito.mock(MenuModel.class));
        Mockito.when(menuQuery.byDateWithoutRelations(start, end))
                .thenReturn(models);
        Mockito.when(mapper.mapToMenu(models))
                .thenReturn(Collections.singletonList(expected));

        List<MenuDTO> menus = menuService.byDateWithoutRelations(start, end);

        assertEquals(1, menus.size());
        MenuDTO menuDTO = menus.get(0);
        assertEquals(expected, menuDTO);
        Mockito.verify(mapper, Mockito.times(1)).mapToMenu(Mockito.anyList());
        Mockito.verify(menuQuery, Mockito.times(1))
                .byDateWithoutRelations(start, end);
    }

    @Test
    public void getDateAndRestaurant() {
        MenuItemDTO expected = stubMenuItemDTO();
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        Mockito.when(menuQuery.byDateWithoutRelations(start, end))
                .thenReturn(Collections.singletonList(Mockito.mock(MenuModel.class)));
        Mockito.when(mapper.mapToMenuItem(Collections.singletonList(Mockito.mock(MenuModel.class))))
                .thenReturn(Collections.singletonList(expected));

        String restaurant = "Restaurant";

        menuService.getDateAndRestaurant(start, end, restaurant);

        Mockito.verify(mapper, Mockito.times(1)).mapToMenuItem(Mockito.anyList());
        Mockito.verify(menuQuery, Mockito.times(1))
                .getDateAndRestaurant(start, end, restaurant);
    }
}
