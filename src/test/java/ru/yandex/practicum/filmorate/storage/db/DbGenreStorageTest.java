package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreRowMapper;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbGenreStorage.class, GenreRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("DbGenreStorage")
class DbGenreStorageTest {
    private final DbGenreStorage genreStorage;

    @Test
    void shouldReturnSixGenres() {
        Collection<Genre> genreList = genreStorage.getAll();
        assertEquals(getAllTestGenres().size(), genreList.size(), "Неверное количество записей жанров");
        assertEquals(getAllTestGenres(), genreList, "Не совпадают наборы записей жанров");
    }

    @Test
    void shouldReturnDramaGenre() {
        Long genreId = 2L;
        Genre dbGenre = genreStorage.getById(genreId);
        assertEquals("Драма", dbGenre.getName(), String.format("Неверное название жанра для ид = %d", genreId));
    }

    private static Collection<Genre> getAllTestGenres() {
        return List.of(
                getGenre(1L, "Комедия"),
                getGenre(2L, "Драма"),
                getGenre(3L, "Мультфильм"),
                getGenre(4L, "Триллер"),
                getGenre(5L, "Документальный"),
                getGenre(6L, "Боевик")
        );
    }

    private static Genre getGenre(Long id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }
}