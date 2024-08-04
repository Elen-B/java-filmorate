package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceprion.DublicateException;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.exceprion.WrongArgumentException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll()
                .stream()
                .peek(this::completeFilmData)
                .toList();
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с ид %s не найден", id));
        }
        return completeFilmData(film);
    }

    public Film add(Film film) {
        log.info("FilmService: add film {}", film);
        return filmStorage.add(completeFilmData(film));
    }

    public Film update(Film film) {
        log.info("FilmService: update film {}", film);
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        checkFilmExist(film.getId());
        return filmStorage.update(completeFilmData(film));
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
        if (film.getUserLikes().contains(user.getId())) {
            throw new DublicateException("Фильм уже отмечен данным пользователем");
        }
        filmStorage.addLike(film, user);
    }

    public void removeLike(Long filmId, Long userId) {
        log.info("FilmService: removeLike film id = {}, user id = {}", filmId, userId);
        checkFilmExist(filmId);
        checkUserExist(userId);
        filmStorage.removeLike(filmId, userId);
    }

    public Collection<Film> getTopLikedFilms(long limit) {
        return filmStorage.getAll()
                .stream()
                .sorted(Comparator.comparing(film -> film.getUserLikes().size(), Comparator.reverseOrder()))
                .limit(limit)
                .toList();

    }

    private Film completeFilmData(Film film) {
        if (film.getMpa() != null) {
            Long mpaId = film.getMpa().getId();
            film.setMpa(checkMpaExist(mpaId));
        }
        film.setGenres(checkGenresExist(film.getGenres()));
        return film;
    }

    private void checkFilmExist(Long filmId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с ид %s не найден", filmId));
        }
    }

    private void checkUserExist(Long userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с ид %s не найден", userId));
        }
    }

    private Mpa checkMpaExist(Long mpaId) {
        if (mpaId == null) {
            return null;
        }
        Mpa mpa = mpaStorage.getById(mpaId);
        if (mpa == null) {
            throw new WrongArgumentException(String.format("Рейтинг с ид %s не найден", mpaId));
        }
        return mpa;
    }

    private Genre checkGenreExist(Long genreId) {
        Genre genre = genreStorage.getById(genreId);
        if (genre == null) {
            throw new WrongArgumentException(String.format("Жанр с ид %s не найден", genreId));
        }
        return genre;
    }

    private List<Genre> checkGenresExist(List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return Collections.emptyList();
        }
        return genres.stream()
                .map(genre -> checkGenreExist(genre.getId()))
                .collect(Collectors.toList());

    }
}
