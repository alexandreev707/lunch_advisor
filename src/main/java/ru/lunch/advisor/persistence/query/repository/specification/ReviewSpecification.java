package ru.lunch.advisor.persistence.query.repository.specification;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.service.State;

import java.time.LocalDateTime;

@RequestScope
@Component
public class ReviewSpecification extends BuilderSpecification<ReviewSpecification, ReviewModel> {

    /**
     * Фильтр по датам
     *
     * @param start дата начала
     * @param end   дата окончания
     */
    public ReviewSpecification between(LocalDateTime start, LocalDateTime end) {
        filter((root, query, builder) -> builder.between(root.get("dateTime"), start, end));
        return this;
    }

    /**
     * Фильтр по ид пользователя
     *
     * @param id ид пользователя
     */
    public ReviewSpecification byUserId(Long id) {
        filter((root, query, builder) -> builder.equal(root.get("user").get("id"), id));
        return this;
    }

    /**
     * Фильтр по ид ресторана
     *
     * @param id ид ресторана
     */
    public ReviewSpecification byRestaurantId(Long id) {
        filter((root, query, builder) -> builder.equal(root.get("menu").get("restaurant").get("id"), id));
        return this;
    }

    /**
     * Без статуса {@link State#DELETED}
     */
    public ReviewSpecification notStateDelete() {
        filter((root, query, builder) -> builder.notEqual(root.get("state"), State.DELETED));
        return this;
    }
}
