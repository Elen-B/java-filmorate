package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User getById(Long id);

    User getByEmail(String email);

    User add(User user);

    User update(User user);

    void delete(Long id);

    void addFriend(User user, User friend);

    void deleteFriend(Long userId, Long friendId);
}
