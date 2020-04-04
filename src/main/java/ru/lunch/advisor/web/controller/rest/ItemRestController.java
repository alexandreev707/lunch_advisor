package ru.lunch.advisor.web.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lunch.advisor.service.ItemService;
import ru.lunch.advisor.service.dto.ItemDTO;
import ru.lunch.advisor.web.mapper.ItemWebMapper;
import ru.lunch.advisor.web.request.ItemRequest;
import ru.lunch.advisor.web.response.ItemView;
import ru.lunch.advisor.web.validation.ApplicationValidation;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = ItemRestController.ITEM_URL)
public class ItemRestController {

    public static final String ITEM_URL = "/api/item";

    private final ItemService itemService;
    private final ItemWebMapper mapper;
    private final ApplicationValidation validator;

    public ItemRestController(ItemService itemService, ItemWebMapper mapper, ApplicationValidation validator) {
        this.itemService = itemService;
        this.mapper = mapper;
        this.validator = validator;
    }

    @GetMapping("/{id}")
    public ItemView get(@PathVariable("id") Long id) {
        return new ItemView(itemService.get(id));
    }

    @GetMapping("/menu/{id}")
    public List<ItemView> getItemsByMenuId(@PathVariable("id") Long menuId) {
        return itemService.getItemsByMenu(menuId)
                .stream()
                .map(ItemView::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ItemView> create(@RequestBody ItemRequest request) {
        validator.validate(request);
        ItemDTO created = itemService.create(mapper.map(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ItemView(created));
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody ItemRequest request) {
        validator.validate(request);
        itemService.update(id, mapper.map(request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        itemService.remove(id);
    }
}
