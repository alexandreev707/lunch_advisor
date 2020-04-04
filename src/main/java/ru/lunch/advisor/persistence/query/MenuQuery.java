package ru.lunch.advisor.persistence.query;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.lunch.advisor.persistence.model.MenuModel;
import ru.lunch.advisor.persistence.query.repository.MenuRepository;
import ru.lunch.advisor.persistence.query.repository.specification.MenuSpecification;

import java.time.LocalDate;
import java.util.List;

/**
 * Компонент по работе с динамическими запросами меню
 */
@Component
public class MenuQuery {

    private final MenuSpecification menuSpecification;
    private final MenuRepository repository;

    public MenuQuery(MenuSpecification menuSpecification, MenuRepository repository) {
        this.menuSpecification = menuSpecification;
        this.repository = repository;
    }

    /**
     * По дате без отзывов и позиций
     *
     * @param start дата начала
     * @param end   дата окончания
     */
    public List<MenuModel> byDateWithoutRelations(LocalDate start, LocalDate end) {
        menuSpecification.reset()
                .between(start, end);
        return repository.findAll(menuSpecification.build());
    }

    /**
     * По дате и ресторану
     *
     * @param start      дата начала
     * @param end        дата окончания
     * @param restaurant ресторан
     */
    public List<MenuModel> getDateAndRestaurant(LocalDate start, LocalDate end, String restaurant) {
        menuSpecification.reset()
                .betweenWithItems(start, end);
        if (!StringUtils.isEmpty(restaurant))
            menuSpecification.byRestaurant(restaurant);
        return repository.findAll(menuSpecification.build());
    }
}
