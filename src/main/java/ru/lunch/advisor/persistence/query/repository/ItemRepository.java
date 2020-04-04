package ru.lunch.advisor.persistence.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.lunch.advisor.persistence.model.ItemModel;

import java.util.List;

/**
 * Репозиторий для работы с позициями меню
 */
@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Long> {

    /**
     * Удалить по идентификаторам
     */
    @Modifying
    @Query("delete from ItemModel i where i.id in :ids")
    void remove(@Param("ids") List<Long> ids);

    /**
     * Удалить связку с меню по ид
     */
    @Modifying
    @Query(value = "delete from items_menu im where im.item_id = :id", nativeQuery = true)
    void removeRelationsMenu(@Param("id") Long id);
}
