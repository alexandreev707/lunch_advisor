package ru.lunch.advisor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.lunch.advisor.persistence.model.ItemModel;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.model.RestaurantModel;
import ru.lunch.advisor.persistence.query.MenuQuery;
import ru.lunch.advisor.persistence.query.repository.MenuRepository;
import ru.lunch.advisor.persistence.query.repository.RestaurantRepository;
import ru.lunch.advisor.service.dto.MenuDTO;
import ru.lunch.advisor.service.dto.MenuFullDTO;
import ru.lunch.advisor.service.dto.MenuItemDTO;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.MenuMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис по работе с меню
 */
@Service
public class MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final MenuQuery menuQuery;
    private final MenuMapper mapper;

    public MenuService(MenuRepository repository, RestaurantRepository restaurantRepository,
                       MenuQuery menuQuery, MenuMapper mapper) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.menuQuery = menuQuery;
        this.mapper = mapper;
    }

    public MenuDTO get(Long id) {
        LOGGER.debug("Get menu by id=[{}]", id);
        MenuModel menuModel = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        return mapper.mapToMenu(menuModel);
    }

    @Transactional
    public MenuDTO create(MenuItemDTO dto) {
        LOGGER.debug("Create menu name=[{}]", dto.getName());
        MenuModel menuModelNew = new MenuModel();
        menuModelNew.setName(dto.getName());
        menuModelNew.setDate(dto.getDate());
        menuModelNew.setItems(getItems(dto));
        menuModelNew.setRestaurant(getRestaurant(dto));

        return mapper.mapToMenu(repository.save(menuModelNew));
    }

    @Transactional
    public void update(Long id, MenuItemDTO dto) {
        LOGGER.debug("Update menu by id=[{}]", id);
        MenuModel menuModel = repository.findById(id).orElseThrow(() -> new NotFoundException(dto.getId()));

        if (dto.getDate() != null)
            menuModel.setDate(dto.getDate());
        if (dto.getName() != null)
            menuModel.setName(dto.getName());
        updateItems(menuModel, dto);

        repository.save(menuModel);
    }

    @Transactional
    public void remove(Long id) {
        LOGGER.debug("Remove menu by id=[{}]", id);
        repository.delete(repository.getOne(id));
    }

    public List<MenuFullDTO> all() {
        LOGGER.debug("Get all menu");
        return mapper.mapToMenuFull(repository.allWithReviews());
    }

    public List<MenuFullDTO> byDateWithItems(LocalDate date) {
        LOGGER.debug("Get all menu equals date=[{}]", date);
        return mapper.mapToMenuFull(repository.eqDateWithReviewItems(date));
    }

    public List<MenuFullDTO> getDate(LocalDate date) {
        LOGGER.debug("Get all menu greater or equals date=[{}]", date);
        return mapper.mapToMenuFull(repository.eqDateWithReviewItems(date));
    }

    public List<MenuDTO> byDateWithoutRelations(LocalDate start, LocalDate end) {
        LOGGER.debug("Get by menu without relations by start=[{}] and end=[{}]", start, end);
        return mapper.mapToMenu(menuQuery.byDateWithoutRelations(start, end));
    }

    public List<MenuItemDTO> getDateAndRestaurant(LocalDate start, LocalDate end, String restaurant) {
        LOGGER.debug("Get by menu with items by start=[{}] and end=[{}], restaurant=[{}]", start, end, restaurant);
        return mapper.mapToMenuItem(menuQuery.getDateAndRestaurant(start, end, restaurant));
    }

    /**
     * Получить ресторан по имени
     */
    private RestaurantModel getRestaurant(MenuItemDTO dto) {
        if (!StringUtils.isEmpty(dto.getRestaurant()))
            return restaurantRepository.getByName(dto.getRestaurant())
                    .orElseThrow(() -> new NotFoundException(dto.getRestaurant()));
        return null;
    }

    /**
     * Получить список позиций для меню
     */
    private Set<ItemModel> getItems(MenuItemDTO dto) {
        if (!CollectionUtils.isEmpty(dto.getItems()))
            return dto.getItems().stream()
                    .map(item -> new ItemModel(item.getName(), item.getPrice()))
                    .collect(Collectors.toSet());
        return null;
    }

    /**
     * Обновление позиций меню
     *
     * @param menuModel меню
     * @param dto       данные для обновления
     */
    private void updateItems(MenuModel menuModel, MenuItemDTO dto) {
        if (dto.getItems() != null) {
            menuModel.getItems().removeIf(itemOld -> dto.getItems().stream()
                    .noneMatch(itemNew -> itemOld.getId().equals(itemNew.getId())));

            List<ItemModel> itemsNew = dto.getItems().stream()
                    .filter(item -> item.getId() == null)
                    .map(item -> new ItemModel(item.getName(), item.getPrice()))
                    .collect(Collectors.toList());

            menuModel.getItems().addAll(itemsNew);
        }
    }
}
