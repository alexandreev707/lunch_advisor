package ru.lunch.advisor.persistence.query.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Билдер для запросов со спецификациями
 *
 * @param <R> тип наследника
 * @param <T> тип модели
 */
public class BuilderSpecification<R extends BuilderSpecification<R, T>, T> {

    private List<Specification<T>> filters = new ArrayList<>(1);

    /**
     * Сброс фильтра
     */
    @SuppressWarnings("unchecked")
    public R reset() {
        filters.clear();
        return (R) this;
    }

    /**
     * Установка фильтра
     */
    public void filter(Specification<T> specification) {
        filters.add(specification);
    }

    /**
     * Получение спецификаций с фильтрами
     */
    public Specification<T> build() {
        return ((root, query, builder) -> {
            Predicate[] predicates = new Predicate[0];
            if (!filters.isEmpty()) {
                predicates = filters.stream()
                        .map(f -> f.toPredicate(root, query, builder))
                        .toArray(Predicate[]::new);
            }
            return builder.and(predicates);
        });
    }
}
