package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();

    Film getById(Long id);

    Film add(Film film);

    Film update(Film film);

    void delete(Long id);

    void addLike(Film film, User user);

    void removeLike(Long filmId, Long userId);
}
