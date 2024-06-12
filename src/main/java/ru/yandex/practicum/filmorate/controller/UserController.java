package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceprion.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult binding) throws BindException {
        if (binding.hasFieldErrors()) {
            binding.getFieldErrors()
                    .forEach(e ->
                            log.error("user create: field: {}, rejected value: {}", e.getField(), e.getRejectedValue()));
            throw new BindException(binding);
        }
        long id = getNextId();
        log.debug("Next id is {}", id);
        user.setId(id);
        user.setName(user.getName());
        users.put(user.getId(), user);
        log.debug("new film is {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser, BindingResult binding) throws BindException {
        if (binding.hasFieldErrors()) {
            binding.getFieldErrors()
                    .forEach(e ->
                            log.error("user update: field: {}, rejected value: {}", e.getField(), e.getRejectedValue()));
            throw new BindException(binding);
        }
        if (newUser == null) {
            log.error("user update: json is null");
            throw new ConditionsNotMetException("Данные о пользователе не переданы");
        }
        if (newUser.getId() == null) {
            log.error("user update: id is null");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            oldUser.setName(newUser.getName());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            log.debug("updated user is {}", oldUser);
            return oldUser;
        }
        log.error("user update: id is not found");
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