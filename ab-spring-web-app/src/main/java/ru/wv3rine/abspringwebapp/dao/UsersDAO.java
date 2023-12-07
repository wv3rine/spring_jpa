package ru.wv3rine.abspringwebapp.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wv3rine.abspringwebapp.models.User;
import ru.wv3rine.abspringwebapp.models.UserIdAndLogin;

import java.util.List;
// Вопрос: по sring data jdbc инфы намного меньше, чем на jpa:
// я его зря использую? Лучше jpa?


// Если я использую взаимодействие только с интерфейсами,
// правильно же, если я комментирую только их?
/**
 * Интерфейс для взаимодействия с базой данных пользователей
 * (класс {@link User})
 */
@Repository
public interface UsersDAO extends JpaRepository<User, Integer> {
    List<UserIdAndLogin> findAllProjectedBy(Sort sort);

    Slice<UserIdAndLogin> findAllProjectedBy(Pageable pageable);
}
