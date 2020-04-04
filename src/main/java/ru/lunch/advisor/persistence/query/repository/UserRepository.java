package ru.lunch.advisor.persistence.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lunch.advisor.persistence.model.UserModel;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    /**
     * Выбрать по почте
     *
     * @param email почта
     */
    Optional<UserModel> getByEmail(String email);
}
