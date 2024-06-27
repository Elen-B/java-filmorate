package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private static Integer globalId = 0;
    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public User add(User user) {
        long id = getNextId();
        //log.debug("Next id is {}", id);
        user.setId(id);
        user.setName(user.getName());
        users.put(user.getId(), user);
        //log.debug("new film is {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            oldUser.setBirthday(user.getBirthday());
            //log.debug("updated user is {}", oldUser);
            return oldUser;
        }
        //log.error("user update: id is not found");
        throw new NotFoundException(String.format("Пользователь с id = %d не найден", user.getId()));
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    private long getNextId() {
        return ++globalId;
    }
}
