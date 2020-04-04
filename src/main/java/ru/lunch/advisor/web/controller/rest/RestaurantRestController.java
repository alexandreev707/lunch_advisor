package ru.lunch.advisor.web.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lunch.advisor.service.RestaurantService;
import ru.lunch.advisor.service.dto.RestaurantDTO;
import ru.lunch.advisor.web.mapper.RestaurantWebMapper;
import ru.lunch.advisor.web.request.RestaurantRequest;
import ru.lunch.advisor.web.response.RestaurantView;
import ru.lunch.advisor.web.validation.ApplicationValidation;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = RestaurantRestController.RESTAURANT_URL)
public class RestaurantRestController {

    public static final String RESTAURANT_URL = "/api/restaurant";

    private final RestaurantService restaurantService;
    private final RestaurantWebMapper mapper;
    private final ApplicationValidation validator;

    public RestaurantRestController(RestaurantService restaurantService, RestaurantWebMapper mapper,
                                    ApplicationValidation validator) {
        this.restaurantService = restaurantService;
        this.mapper = mapper;
        this.validator = validator;
    }

    @GetMapping(value = "/{id}")
    public RestaurantView get(@PathVariable("id") Long id) {
        return new RestaurantView(restaurantService.get(id));
    }

    @GetMapping
    public List<RestaurantView> all() {
        return restaurantService.all()
                .stream()
                .map(RestaurantView::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<RestaurantView> create(@RequestBody RestaurantRequest request) {
        validator.validate(request);
        RestaurantDTO created = restaurantService.create(mapper.map(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new RestaurantView(created));
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody RestaurantRequest request) {
        validator.validate(request);
        restaurantService.update(id, mapper.map(request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        restaurantService.remove(id);
    }
}
