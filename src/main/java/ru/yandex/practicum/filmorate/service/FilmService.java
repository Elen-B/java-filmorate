package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if (film == null) {
            throw new ConditionsNotMetException("Данные о фильме не переданы");
        }
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        return filmStorage.update(film);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }
}
