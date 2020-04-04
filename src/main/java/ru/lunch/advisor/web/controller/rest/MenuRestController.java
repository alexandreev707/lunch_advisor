package ru.lunch.advisor.web.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.lunch.advisor.service.MenuService;
import ru.lunch.advisor.service.dto.MenuDTO;
import ru.lunch.advisor.web.mapper.MenuWebMapper;
import ru.lunch.advisor.web.request.MenuCreateRequest;
import ru.lunch.advisor.web.request.MenuUpdateRequest;
import ru.lunch.advisor.web.response.MenuItemView;
import ru.lunch.advisor.web.response.MenuReviewView;
import ru.lunch.advisor.web.response.MenuView;
import ru.lunch.advisor.web.validation.ApplicationValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = MenuRestController.MENU_URL)
public class MenuRestController {

    public static final String MENU_URL = "/api/menu";

    private final MenuService menuService;
    private final MenuWebMapper menuMapper;
    private final ApplicationValidation validator;


    public MenuRestController(MenuService menuService, MenuWebMapper menuMapper, ApplicationValidation validator) {
        this.menuService = menuService;
        this.menuMapper = menuMapper;
        this.validator = validator;
    }

    @GetMapping("{id}")
    public MenuView get(@PathVariable("id") Long id) {
        return new MenuView(menuService.get(id));
    }

    @GetMapping
    public List<MenuReviewView> all() {
        return menuService.getDate(LocalDate.now())
                .stream()
                .map(MenuReviewView::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/byDate")
    public List<MenuItemView> byDate(@RequestParam @Nullable LocalDate start,
                                     @RequestParam @Nullable LocalDate end,
                                     @RequestParam @Nullable String restaurant) {
        return menuService.getDateAndRestaurant(start, end, restaurant)
                .stream()
                .map(MenuItemView::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<MenuView> create(@RequestBody MenuCreateRequest request) {
        validator.validate(request);
        MenuDTO created = menuService.create(menuMapper.mapToMenuItem(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new MenuView(created));
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody MenuUpdateRequest request) {
        validator.validate(request);
        menuService.update(id, menuMapper.mapToMenuItem(request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        menuService.remove(id);
    }
}
