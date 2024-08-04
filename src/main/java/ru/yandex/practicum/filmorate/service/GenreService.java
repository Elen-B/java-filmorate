package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceprion.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getAllGenre() {
        return genreStorage.getAll();
    }

    public Genre getGenreById(Long id) {
        Genre genre = genreStorage.getById(id);
        if (genre == null) {
            throw new NotFoundException(String.format("Жанр с ид %s не найден", id));
        }
        return genre;
    }
}
