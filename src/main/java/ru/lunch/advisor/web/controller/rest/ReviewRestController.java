package ru.lunch.advisor.web.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lunch.advisor.service.ReviewService;
import ru.lunch.advisor.service.dto.ReviewDTO;
import ru.lunch.advisor.web.mapper.ReviewWebMapper;
import ru.lunch.advisor.web.request.ReviewRequest;
import ru.lunch.advisor.web.response.ReviewUserView;
import ru.lunch.advisor.web.response.ReviewView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = ReviewRestController.REVIEW_URL)
public class ReviewRestController {

    public static final String REVIEW_URL = "/api/review";

    private final ReviewService reviewService;
    private final ReviewWebMapper reviewMapper;

    public ReviewRestController(ReviewService reviewService, ReviewWebMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @GetMapping("/{id}")
    public ReviewView get(@PathVariable Long id) {
        return new ReviewView(reviewService.get(id));
    }

    @GetMapping
    public List<ReviewView> all() {
        return reviewService.all()
                .stream()
                .map(ReviewView::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{id}")
    public List<ReviewUserView> byUserId(@PathVariable Long id) {
        return reviewService.getByUserId(id)
                .stream()
                .map(ReviewUserView::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/menu/{id}")
    public ReviewView getByMenuId(@PathVariable Long id) {
        return new ReviewView(reviewService.getByMenuId(id));
    }

    @PostMapping
    public ResponseEntity<ReviewView> create(@RequestBody ReviewRequest request) {
        ReviewDTO created = reviewService.create(reviewMapper.map(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewView(created));
    }

    @PostMapping("/restaurant/{id}")
    public ResponseEntity<ReviewView> createByRestaurantId(@PathVariable("id") Long id,
                                                           @RequestBody ReviewRequest request) {
        ReviewDTO created = reviewService.createByRestaurant(id, reviewMapper.map(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewView(created));
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody ReviewRequest request) {
        reviewService.update(id, reviewMapper.map(request));
    }

    @PutMapping("/restaurant/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateByRestaurant(@PathVariable("id") Long id, @RequestBody ReviewRequest request) {
        reviewService.updateByRestaurant(id, reviewMapper.map(request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        reviewService.remove(id);
    }
}
