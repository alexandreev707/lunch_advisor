package ru.lunch.advisor.persistence.query.repository.specification;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.lunch.advisor.persistence.model.MenuModel;

import javax.persistence.criteria.*;
import java.time.LocalDate;

/**
 * Спецификация для работы с меню
 */
@RequestScope
@Component
public class MenuSpecification extends BuilderSpecification<MenuSpecification, MenuModel> {

    /**
     * Фильтр по датам c позициями меню
     *
     * @param start дата начала
     * @param end   дата окончания
     */
    public MenuSpecification betweenWithItems(LocalDate start, LocalDate end) {
        filter((root, query, builder) -> {
            root.fetch("items", JoinType.LEFT);
            return between(root, query, builder, start, end);
        });
        return this;
    }

    /**
     * Фильтр по датам
     *
     * @param start дата начала
     * @param end   дата окончания
     */
    public MenuSpecification between(LocalDate start, LocalDate end) {
        filter((root, query, builder) -> between(root, query, builder, start, end));
        return this;
    }

    /**
     * Фильтр по дате
     *
     * @param date дата
     */
    public MenuSpecification byDate(LocalDate date) {
        filter((root, query, builder) -> builder.equal(root.get("date"), date));
        return this;
    }

    /**
     * Фильтр по рестарану
     *
     * @param name наименование ресторана
     */
    public MenuSpecification byRestaurant(String name) {
        filter((root, query, builder) -> builder.equal(root.get("restaurant").get("name"), name));
        return this;
    }

    /**
     * Фильтр по ид ресторана
     *
     * @param id ид ресторана
     */
    public MenuSpecification byRestaurantId(Long id) {
        filter((root, query, builder) -> builder.equal(root.get("restaurant").get("id"), id));
        return this;
    }

    /**
     * Фильтр по датам
     *
     * @param start дата начала
     * @param end   дата окончания
     */
    private Predicate between(Root<MenuModel> root, CriteriaQuery query, CriteriaBuilder builder, LocalDate start,
                              LocalDate end) {
        query.distinct(true);
        query.orderBy(builder.asc(root.get("id")));
        if (start != null && end != null)
            return builder.between(root.get("date"), start, end);
        return builder.between(root.get("date"), LocalDate.now(), LocalDate.now());
    }
}
