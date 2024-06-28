package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film add(Film film) {
        log.info("FilmService: add film {}", film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        log.info("FilmService: update film {}", film);
        if (film == null) {
            throw new ConditionsNotMetException("Данные о фильме не переданы");
        }
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        return filmStorage.update(film);
    }

    public void delete(Long id) {
        log.info("FilmService: delete film id = {}", id);
        filmStorage.delete(id);
    }

    public void addLike(Long filmId, Long userId) {
        log.info("FilmService: addLike film id = {}, user id = {}", filmId, userId);
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с ид %s не найден", filmId));
        }
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", userId));
        }
        film.getUserLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("FilmService: removeLike film id = {}, user id = {}", filmId, userId);
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с ид %s не найден", filmId));
        }
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", userId));
        }
        film.getUserLikes().remove(userId);
    }

    public Collection<Film> getTopLikedFilms(long limit) {
        return filmStorage.getAll()
                .stream()
                .sorted(Comparator.comparing(film -> film.getUserLikes().size(), Comparator.reverseOrder()))
                .limit(limit)
                .toList();

    }
}
