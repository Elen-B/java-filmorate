package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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
    public Film create(@Valid @RequestBody Film film, BindingResult binding) throws BindException {
        if (binding.hasFieldErrors()) {
            binding.getFieldErrors()
                    .forEach(e ->
                            log.error("film create: field: {}, rejected value: {}", e.getField(), e.getRejectedValue()));
            throw new BindException(binding);
        }
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody(required = false) Film newFilm, BindingResult binding) throws BindException {
        if (binding.hasFieldErrors()) {
            binding.getFieldErrors()
                    .forEach(e ->
                            log.error("film update: field: {}, rejected value: {}", e.getField(), e.getRejectedValue()));
            throw new BindException(binding);
        }
        return filmService.update(newFilm);
    }
}