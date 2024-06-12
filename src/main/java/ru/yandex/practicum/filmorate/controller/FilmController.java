package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceprion.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult binding) throws BindException {
        if (binding.hasFieldErrors()) {
            binding.getFieldErrors()
                    .forEach(e ->
                            log.error("film create: field: {}, rejected value: {}", e.getField(), e.getRejectedValue()));
            throw new BindException(binding);
        }
        long id = getNextId();
        log.debug("Next id is {}", id);
        film.setId(id);
        films.put(film.getId(), film);
        log.debug("new film is {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm, BindingResult binding) throws BindException {
        if (binding.hasFieldErrors()) {
            binding.getFieldErrors()
                    .forEach(e ->
                            log.error("film update: field: {}, rejected value: {}", e.getField(), e.getRejectedValue()));
            throw new BindException(binding);
        }
        if (newFilm == null) {
            log.error("film update: json is null");
            throw new ConditionsNotMetException("Данные о фильме не переданы");
        }
        if (newFilm.getId() == null) {
            log.error("film update: id is null");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.debug("new film is {}", oldFilm);
            return oldFilm;
        }
        log.error("film update: id is not found");
        throw new NotFoundException(String.format("Фильм с id = %d не найден", newFilm.getId()));
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}