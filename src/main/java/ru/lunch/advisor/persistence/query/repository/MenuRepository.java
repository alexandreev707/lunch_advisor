package ru.lunch.advisor.persistence.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.lunch.advisor.persistence.model.MenuModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с меню
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuModel, Long>, JpaSpecificationExecutor<MenuModel> {

    /**
     * Получить все уникальные записи
     */
    @Query("select distinct m from MenuModel m left join fetch m.reviews r")
    List<MenuModel> allWithReviews();

    /**
     * Получить все уникальные записи на дату с отзывами и позициями
     *
     * @param date дата
     */
    @Query("select distinct m from MenuModel m left join fetch m.reviews r left join fetch m.items i where m.date = :date " +
            "order by m.id asc ")
    List<MenuModel> eqDateWithReviewItems(@Param("date") LocalDate date);

    /**
     * Получить меню по ид ресторана и дате
     *
     * @param date         дата
     * @param restaurantId ид ресторана
     */
    @Query("select m from MenuModel m where m.date = :date and m.restaurant.id = :restaurantId")
    Optional<MenuModel> byDateAndRestaurant(@Param("date") LocalDate date, @Param("restaurantId") Long restaurantId);

    /**
     * Получить меню по ид с позициями
     *
     * @param id ид меню
     */
    @Query("select m from MenuModel m left join fetch m.items i where m.id = :id")
    Optional<MenuModel> byIdWithItems(@Param("id") Long id);
}
