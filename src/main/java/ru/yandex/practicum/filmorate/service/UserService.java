package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        if (user == null) {
            log.error("user update: json is null");
            throw new ConditionsNotMetException("Данные о пользователе не переданы");
        }
        if (user.getId() == null) {
            log.error("user update: id is null");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        return userStorage.update(user);
    }

    void delete(Long id) {
        userStorage.delete(id);
    }
}
