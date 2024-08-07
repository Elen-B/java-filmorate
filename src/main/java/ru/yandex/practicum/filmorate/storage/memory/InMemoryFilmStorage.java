package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static Integer globalId = 0;

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getById(Long id) {
        return films.get(id);
    }

    @Override
    public Film add(Film film) {
        long id = getNextId();
        film.setId(id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());
            return oldFilm;
        }
        throw new NotFoundException(String.format("Фильм с id = %d не найден", film.getId()));
    }

    @Override
    public void delete(Long id) {
        films.remove(id);
    }

    @Override
    public void addLike(Film film, User user) {
        film.getUserLikes().add(user.getId());
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        films.get(filmId).getUserLikes().remove(userId);
    }

    private long getNextId() {
        return ++globalId;
    }
}
