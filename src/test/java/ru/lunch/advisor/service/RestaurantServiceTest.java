package ru.lunch.advisor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.lunch.advisor.persistence.model.RestaurantModel;
import ru.lunch.advisor.persistence.query.repository.RestaurantRepository;
import ru.lunch.advisor.service.dto.RestaurantDTO;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.RestaurantMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;

public class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository repository;
    @Mock
    private RestaurantMapper mapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void get() {
        RestaurantDTO expected = stubRestaurantDTO();
        Mockito.when(repository.findById(50L)).thenReturn(Optional.of(Mockito.mock(RestaurantModel.class)));
        Mockito.when(mapper.map(Mockito.any(RestaurantModel.class))).thenReturn(expected);

        RestaurantDTO dto = restaurantService.get(50L);

        assertEquals(expected.getAddress(), dto.getAddress());
        assertEquals(expected.getName(), dto.getName());
        assertEquals(expected.getId(), dto.getId());

        Mockito.verify(repository, Mockito.times(1)).findById(50L);
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.any(RestaurantModel.class));
    }

    @Test
    public void getException() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(50L));
    }

    @Test
    public void all() {
        restaurantService.all();
        Mockito.verify(repository, Mockito.times(1)).findAll();
        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.anyList());
    }

    @Test
    public void create() {
        RestaurantModel restaurantModel = Mockito.any(RestaurantModel.class);
        Mockito.when(repository.save(restaurantModel)).thenReturn(restaurantModel);

        restaurantService.create(new RestaurantDTO());

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(RestaurantModel.class));
        Mockito.verify(mapper, Mockito.times(1)).map(restaurantModel);
    }

    @Test
    public void update() {
        RestaurantModel restaurantModel = Mockito.mock(RestaurantModel.class);
        Mockito.when(repository.findById(50L)).thenReturn(Optional.of(restaurantModel));
        Mockito.when(mapper.map(restaurantModel)).thenReturn(new RestaurantDTO());

        restaurantService.update(50L, new RestaurantDTO());

        Mockito.verify(restaurantModel, Mockito.times(1)).setAddress(Mockito.any());
        Mockito.verify(restaurantModel, Mockito.times(1)).setName(Mockito.any());
        Mockito.verify(repository, Mockito.times(1)).findById(50L);
        Mockito.verify(repository, Mockito.times(1)).save(restaurantModel);
    }

    @Test
    public void updateException() {
        assertThrows(NotFoundException.class, () -> restaurantService.update(50L, new RestaurantDTO()));
    }

    @Test
    public void remove() {
        repository.deleteById(50L);
        Mockito.verify(repository, Mockito.times(1)).deleteById(50L);
    }

    private RestaurantDTO stubRestaurantDTO() {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setAddress("Victory");
        dto.setName("Sea");
        dto.setId(50L);

        return dto;
    }
}
