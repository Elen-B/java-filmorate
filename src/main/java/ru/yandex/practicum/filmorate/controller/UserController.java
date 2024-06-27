package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult binding) throws BindException {
        if (binding.hasFieldErrors()) {
            binding.getFieldErrors()
                    .forEach(e ->
                            log.error("user create: field: {}, rejected value: {}", e.getField(), e.getRejectedValue()));
            throw new BindException(binding);
        }
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser, BindingResult binding) throws BindException {
        if (binding.hasFieldErrors()) {
            binding.getFieldErrors()
                    .forEach(e ->
                            log.error("user update: field: {}, rejected value: {}", e.getField(), e.getRejectedValue()));
            throw new BindException(binding);
        }
        return userService.updateUser(newUser);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long userId, @PathVariable("friendId") long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long userId, @PathVariable("friendId") long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable("id") long userId) {
        return userService.getAllFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") long userId, @PathVariable("otherId") long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }
}