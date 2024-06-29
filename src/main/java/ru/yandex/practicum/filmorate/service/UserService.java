package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(Long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", id));
        }
        return user;
    }

    public User addUser(User user) {
        log.info("UserService: addUser user = {}", user);
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        log.info("UserService: updateUser user = {}", user);
        if (user.getId() == null) {
            log.error("user update: id is null");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        return userStorage.update(user);
    }

    public void deleteUser(Long id) {
        log.info("UserService: deleteUser user id = {}", id);
        userStorage.delete(id);
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("UserService: addFriend user id = {}, friend id = {}", userId, friendId);
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", userId));
        }
        if (friend == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", friendId));
        }
        if (Objects.equals(userId, friendId)) {
            throw new IllegalArgumentException("Ид друга совпадает с ид пользователя");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        log.info("UserService: deleteFriend user id = {}, friend id = {}", userId, friendId);
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", userId));
        }
        if (friend == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", friendId));
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> getAllFriends(Long userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", userId));
        }
        return user.getFriends()
                .stream()
                .map(userStorage::getById)
                .toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherUserId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", userId));
        }
        if (otherUser == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", otherUserId));
        }
        return user.getFriends()
                .stream()
                .filter(id -> otherUser.getFriends().contains(id))
                .map(userStorage::getById)
                .toList();
    }
}
