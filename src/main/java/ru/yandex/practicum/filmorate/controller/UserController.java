package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        //добавить валидацию
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser == null) {
            //исключение
        }
        if (newUser.getId() == null) {
            //исключение
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            //добавить валидацию полей
            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;
        }
        throw new NotFoundException(String.format("Пользователь с id = %d не найден", newUser.getId()));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}