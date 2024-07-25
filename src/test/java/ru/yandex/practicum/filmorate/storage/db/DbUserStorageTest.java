package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbUserStorage.class, UserRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("DbUserStorage")
class DbUserStorageTest {
    private final DbUserStorage userStorage;

    @Test
    void shouldGetTwoUsers() {
        userStorage.add(getTestUser());
        userStorage.add(getTestUser2());
        assertEquals(2, userStorage.getAll().size(), "Неверное общее количество пользователей");
    }

    @Test
    void shouldGetUserById() {
        User user = getTestUser();
        userStorage.add(user);
        userStorage.add(getTestUser2());
        Long id = user.getId();
        User resultUser = userStorage.getById(id);
        assertNotNull(resultUser, "неудалось получить пользователя по ид");
        assertEquals(user.getEmail(), resultUser.getEmail(), "Неверный email полученного пользователя");
        assertEquals(user.getLogin(), resultUser.getLogin(), "Неверный логин полученного пользователя");
        assertEquals(user.getName(), resultUser.getName(), "Неверное имя полученного пользователя");
        assertEquals(user.getBirthday(), resultUser.getBirthday(), "Неверная дата рождения полученного пользователя");
    }

    @Test
    void shouldGetUserByEmail() {
        User user = getTestUser();
        userStorage.add(user);
        userStorage.add(getTestUser2());
        String email = user.getEmail().toUpperCase();
        User resultUser = userStorage.getByEmail(email);
        assertNotNull(resultUser, "неудалось получить пользователя по email");
        assertEquals(user.getId(), resultUser.getId(), "Неверный ид полученного пользователя");
        assertEquals(user.getLogin(), resultUser.getLogin(), "Неверный логин полученного пользователя");
        assertEquals(user.getName(), resultUser.getName(), "Неверное имя полученного пользователя");
        assertEquals(user.getBirthday(), resultUser.getBirthday(), "Неверная дата рождения полученного пользователя");
    }

    @Test
    void shouldAddUser() {
        User user = getTestUser();
        userStorage.add(user);
        Long id = user.getId();
        User resultUser = userStorage.getById(id);
        assertNotNull(resultUser);
        assertEquals(user.getEmail(), resultUser.getEmail(), "Неверный email добавленного пользователя");
        assertEquals(user.getLogin(), resultUser.getLogin(), "Неверный логин добавленного пользователя");
        assertEquals(user.getName(), resultUser.getName(), "Неверное имя добавленного пользователя");
        assertEquals(user.getBirthday(), resultUser.getBirthday(), "Неверная дата рождения добавленного пользователя");
    }

    @Test
    void shouldUpdateUser() {
        User user = getTestUser();
        userStorage.add(user);
        Long id = user.getId();
        user.setEmail("new@email.com");
        user.setLogin("newLogin");
        user.setName("NEW");
        user.setBirthday(LocalDate.of(1990, 12, 12));
        userStorage.update(user);
        User resultUser = userStorage.getById(id);
        assertNotNull(resultUser);
        assertEquals(user.getEmail(), resultUser.getEmail(), "Неверный email после обновления пользователя");
        assertEquals(user.getLogin(), resultUser.getLogin(), "Неверный логин после обновления пользователя");
        assertEquals(user.getName(), resultUser.getName(), "Неверное имя после обновления пользователя");
        assertEquals(user.getBirthday(), resultUser.getBirthday(), "Неверная дата рождения после обновления пользователя");
    }

    @Test
    void shouldDeleteUser() {
        User user = getTestUser();
        userStorage.add(user);
        Long id = user.getId();
        userStorage.delete(id);
        assertNull(userStorage.getById(id), "пользователь не удален");
    }

    @Test
    void shouldAddFriend() {
        User user = getTestUser();
        User friend = getTestUser2();
        userStorage.add(user);
        userStorage.add(friend);
        Long id = user.getId();
        Long friendId = friend.getId();
        userStorage.addFriend(user, friend);
        User resultUser = userStorage.getById(id);
        User resultFriend = userStorage.getById(friendId);
        assertEquals(1, resultUser.getFriends().size(), "друг не добавлен");
        assertEquals(0, resultFriend.getFriends().size(), "дружба должна быть односторонней");
    }

    @Test
    void shouldDeleteFriend() {
        User user = getTestUser();
        User friend = getTestUser2();
        userStorage.add(user);
        userStorage.add(friend);
        Long id = user.getId();
        Long friendId = friend.getId();
        userStorage.addFriend(user, friend);
        userStorage.addFriend(friend, user);
        userStorage.deleteFriend(id, friendId);
        User resultUser = userStorage.getById(id);
        User resultFriend = userStorage.getById(friendId);
        assertEquals(0, resultUser.getFriends().size(), "друг не удален");
        assertEquals(1, resultFriend.getFriends().size(), "у друга не должен измениться список друзей");
    }

    private static User getTestUser() {
        User user = new User();
        user.setEmail("user1@mail.ru");
        user.setLogin("userrrr");
        user.setName("user User");
        user.setBirthday(LocalDate.of(2005, 5, 1));
        user.setFriends(Set.of(2L, 3L));
        return user;
    }

    private static User getTestUser2() {
        User user = new User();
        user.setEmail("user2@mail.ru");
        user.setLogin("aaa");
        user.setName("Ya");
        user.setBirthday(LocalDate.of(2001, 12, 7));
        user.setFriends(Set.of(1L));
        return user;
    }
}