package ru.lunch.advisor.persistence.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.lunch.advisor.persistence.model.ReviewModel;
import ru.lunch.advisor.service.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с отзывами
 */
@Repository
public interface ReviewRepository extends JpaRepository<ReviewModel, Long>, JpaSpecificationExecutor<ReviewModel> {

    /**
     * Выбрать по дате ид пользователя со статусом {@link State#ACTIVE}
     *
     * @param dateTime дата/время начала
     * @param endDate  дата/время окончания
     * @param userId   ид пользователя
     */
    @Query("select r from REVIEW r where r.dateTime >= :startDate and r.dateTime <= :endDate" +
            " and r.user.id = :userId and r.state = 'ACTIVE'")
    List<ReviewModel> getByUserIdAndBetween(@Param("startDate") LocalDateTime dateTime,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("userId") Long userId);

    /**
     * Выбрать по ид пользователя и ид меню
     *
     * @param menuId ид меню
     * @param userId ид пользователя
     */
    @Query("select r from REVIEW r where r.menu.id = :menuId and r.user.id = :userId")
    Optional<ReviewModel> getByUserAndMenu(@Param("menuId") Long menuId,
                                           @Param("userId") Long userId);

    /**
     * Пометить статусом {@link State#DELETED}
     *
     * @param ids список ид
     */
    @Modifying
    @Query("update REVIEW r set r.state = 'DELETED' where r.id in :ids")
    void deleteVote(@Param("ids") List<Long> ids);

    /**
     * Выбрать по ид пользователя
     *
     * @param id ид пользователя
     */
    @Query("select r from REVIEW r where r.user.id = :id")
    List<ReviewModel> byUserId(@Param("id") Long id);

    /**
     * Удалить по ид пользователя
     *
     * @param id ид пользователя
     */
    @Modifying
    @Query("delete from REVIEW r where r.user.id = :id")
    void removeByUserId(@Param("id") Long id);
}
