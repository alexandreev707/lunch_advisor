package ru.lunch.advisor.persistence.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.lunch.advisor.persistence.model.RestaurantModel;

import java.util.Optional;

/**
 * Репозиторий для работы c ресторанами
 */
@Transactional(readOnly = true)
@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantModel, Long> {

    /**
     * Выбрать по имени
     *
     * @param name имя
     */
    @Query("select r from RestaurantModel r where r.name = :name")
    Optional<RestaurantModel> getByName(@Param("name") String name);
}
