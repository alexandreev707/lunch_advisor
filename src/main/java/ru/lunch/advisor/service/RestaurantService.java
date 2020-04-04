package ru.lunch.advisor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lunch.advisor.persistence.model.RestaurantModel;
import ru.lunch.advisor.persistence.query.repository.RestaurantRepository;
import ru.lunch.advisor.service.dto.RestaurantDTO;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.RestaurantMapper;

import java.util.List;

/**
 * Сервис по работе с ресторанами
 */
@Service
public class RestaurantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository repository;
    private final MenuService menuService;
    private final RestaurantMapper mapper;

    public RestaurantService(RestaurantRepository repository, RestaurantMapper mapper, MenuService menuService) {
        this.repository = repository;
        this.mapper = mapper;
        this.menuService = menuService;
    }

    public RestaurantDTO get(Long id) {
        LOGGER.debug("Get restaurant by id=[{}]", id);
        return mapper.map(repository.findById(id).orElseThrow(() -> new NotFoundException(id)));
    }

    public List<RestaurantDTO> all() {
        LOGGER.debug("Get all restaurants");
        return mapper.map(repository.findAll());
    }

    @Transactional
    public RestaurantDTO create(RestaurantDTO dto) {
        LOGGER.debug("Create restaurant");
        RestaurantModel model = new RestaurantModel();
        model.setAddress(dto.getAddress());
        model.setName(dto.getName());

        return mapper.map(repository.save(model));
    }

    @Transactional
    public RestaurantModel update(Long id, RestaurantDTO dto) {
        LOGGER.debug("Update restaurant");

        RestaurantModel restaurantModel = repository.findById(id).orElseThrow(NotFoundException::new);
        restaurantModel.setAddress(dto.getAddress());
        restaurantModel.setName(dto.getName());

        return repository.save(restaurantModel);
    }

    @Transactional
    public void remove(Long id) {
        LOGGER.debug("Remove restaurant by id=[{}]", id);
        repository.delete(repository.getOne(id));
    }
}
