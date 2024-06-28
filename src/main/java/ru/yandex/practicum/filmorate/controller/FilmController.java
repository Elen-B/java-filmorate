package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody(required = false) Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Long limit
    ) {
        return filmService.getTopLikedFilms(limit);
    }
}