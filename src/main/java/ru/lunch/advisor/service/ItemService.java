package ru.lunch.advisor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lunch.advisor.persistence.model.ItemModel;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.query.repository.ItemRepository;
import ru.lunch.advisor.persistence.query.repository.MenuRepository;
import ru.lunch.advisor.service.dto.ItemDTO;
import ru.lunch.advisor.service.exeption.NotFoundException;
import ru.lunch.advisor.service.mapper.ItemMapper;

import java.util.Set;

/**
 * Сервис по работе с позициями меню
 */
@Service
public class ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository repository;
    private final MenuRepository menuRepository;
    private final ItemMapper mapper;

    public ItemService(ItemRepository repository, MenuRepository menuRepository, ItemMapper mapper) {
        this.repository = repository;
        this.menuRepository = menuRepository;
        this.mapper = mapper;
    }

    public ItemDTO get(Long id) {
        LOGGER.debug("Get item by id=[{}]", id);
        return mapper.map(repository.findById(id).orElseThrow(() -> new NotFoundException(id)));
    }

    public Set<ItemDTO> all() {
        LOGGER.debug("Get all items");
        return mapper.map(repository.findAll());
    }

    public Set<ItemDTO> getItemsByMenu(Long menuId) {
        LOGGER.debug("Get items by menu id=[{}]", menuId);
        MenuModel menuModel = menuRepository.byIdWithItems(menuId).orElseThrow(() -> new NotFoundException(menuId));
        return mapper.map(menuModel.getItems());
    }

    @Transactional
    public ItemDTO create(ItemDTO dto) {
        LOGGER.debug("Create item");
        ItemModel itemModel = new ItemModel();
        itemModel.setName( dto.getName());
        itemModel.setPrice(dto.getPrice());

        ItemModel itemNew = repository.save(itemModel);

        return mapper.map(itemNew);
    }

    @Transactional
    public void update(Long id, ItemDTO dto) {
        LOGGER.debug("Update item by id=[{}]", id);

        ItemModel itemModel = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        itemModel.setName(dto.getName());
        itemModel.setPrice(dto.getPrice());

        repository.save(itemModel);
    }

    @Transactional
    public void remove(Long id) {
        LOGGER.debug("Remove item by id=[{}]", id);
        repository.removeRelationsMenu(id);
        repository.deleteById(id);
    }
}