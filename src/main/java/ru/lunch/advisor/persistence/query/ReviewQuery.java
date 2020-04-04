package ru.lunch.advisor.persistence.query;

import org.springframework.stereotype.Component;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.persistence.query.repository.ReviewRepository;
import ru.lunch.advisor.persistence.query.repository.specification.ReviewSpecification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Компонент по работе с динамическими запросами отзывов
 */
@Component
public class ReviewQuery {

    private final ReviewSpecification reviewSpecification;
    private final ReviewRepository repository;

    public ReviewQuery(ReviewSpecification reviewSpecification, ReviewRepository repository) {
        this.reviewSpecification = reviewSpecification;
        this.repository = repository;
    }

    /**
     * Получить по ид ресторана, ид пользователя и периоду
     *
     * @param restaurantId ид ресторана
     * @param userId       ид пользователя
     * @param start        дата начала
     * @param end          дата окончания
     */
    public List<ReviewModel> getByRestaurantId(Long restaurantId, Long userId, LocalDateTime start, LocalDateTime end) {
        reviewSpecification.reset()
                .byRestaurantId(restaurantId);
        reviewSpecification.between(start, end);
        if (userId != null) reviewSpecification.byUserId(userId);

        return repository.findAll(reviewSpecification.build());
    }

    /**
     * Получить по ид пользователя на дату
     *
     * @param id   ид пользователя
     * @param date дата
     */
    public List<ReviewModel> getByUserId(Long id, LocalDate date) {

        reviewSpecification.reset()
                .between(date.atTime(LocalTime.MIN), date.atTime(LocalTime.MAX))
                .notStateDelete();
        if (id != null) reviewSpecification.byUserId(id);

        return repository.findAll(reviewSpecification.build());
    }
}
