package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.mappers.FilmRowMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbFilmStorage.class, FilmRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("DbFilmStorage")
class DbFilmStorageTest {
    private final DbFilmStorage filmStorage;

    @Test
    void shouldReturnTwoFilms() {
        Film film = getTestFilm();
        filmStorage.add(film);
        filmStorage.add(film);
        assertEquals(2, filmStorage.getAll().size(), "Неверное общее количество фильмов");
    }

    @Test
    void shouldGetFilmById() {
        Film film = getTestFilm();
        filmStorage.add(film);
        filmStorage.add(getTestFilm2());
        Long id = film.getId();
        Film resultFilm = filmStorage.getById(id);
        assertNotNull(resultFilm, "Не удалось получить фильм по ид");
        assertEquals(film.getName(), resultFilm.getName(), "Неверное наименование полученного фильма");
        assertEquals(film.getDescription(), resultFilm.getDescription(), "Неверное описание полученного фильма");
        assertEquals(film.getDuration(), resultFilm.getDuration(), "Неверная длительность полученного фильма");
        assertEquals(film.getReleaseDate(), resultFilm.getReleaseDate(), "Неверная дата выпуска полученного фильма");
        assertEquals(film.getMpa().getId(), resultFilm.getMpa().getId(), "Неверный рейтинг полученного фильма");
    }

    @Test
    void shouldAddNewFilm() {
        Film film = getTestFilm();
        filmStorage.add(film);
        Long id = film.getId();
        Film resultFilm = filmStorage.getById(id);
        assertNotNull(resultFilm, "Фильм не добавлен");
        assertEquals(film.getName(), resultFilm.getName(), "Неверное наименование добавленного фильма");
        assertEquals(film.getDescription(), resultFilm.getDescription(), "Неверное описание добавленного фильма");
        assertEquals(film.getDuration(), resultFilm.getDuration(), "Неверная длительность добавленного фильма");
        assertEquals(film.getReleaseDate(), resultFilm.getReleaseDate(), "Неверная дата выпуска добавленного фильма");
        assertEquals(film.getMpa().getId(), resultFilm.getMpa().getId(), "Неверный рейтинг добавленного фильма");
    }

    @Test
    void shouldUpdateFilm() {
        Film film = getTestFilm();
        filmStorage.add(film);
        Long id = film.getId();
        Mpa mpa = getMpa(2L, "PG");
        film.setName("update film");
        film.setDescription("update description");
        film.setDuration(50L);
        film.setMpa(mpa);
        film.setReleaseDate(LocalDate.of(1999, 10, 11));
        filmStorage.update(film);
        Film resultFilm = filmStorage.getById(id);
        assertNotNull(resultFilm, "Обновленный фильма не найден");
        assertEquals(film.getName(), resultFilm.getName(), "Неверное наименование после обновления фильма");
        assertEquals(film.getDescription(), resultFilm.getDescription(), "Неверное описание после обновления фильма");
        assertEquals(film.getDuration(), resultFilm.getDuration(), "Неверная длительность после обновления фильма");
        assertEquals(film.getReleaseDate(), resultFilm.getReleaseDate(), "Неверная дата выпуска после обновления фильма");
        assertEquals(film.getMpa().getId(), resultFilm.getMpa().getId(), "Неверный рейтинг после обновления фильма");
    }

    @Test
    void shouldDeleteFilm() {
        Film film = getTestFilm();
        filmStorage.add(film);
        Long id = film.getId();
        filmStorage.delete(id);
        assertNull(filmStorage.getById(id), "фильм не удален");
    }

    private static Film getTestFilm() {
        Mpa mpa = getMpa(1L, "G");
        Film film = new Film();
        film.setName("new film");
        film.setDescription("it's wonderful");
        film.setDuration(99L);
        film.setMpa(mpa);
        film.setReleaseDate(LocalDate.of(2000, 12, 7));
        return film;
    }

    private static Film getTestFilm2() {
        Mpa mpa = getMpa(2L, "PG");
        Film film = new Film();
        film.setName("old film");
        film.setDuration(15L);
        film.setMpa(mpa);
        film.setReleaseDate(LocalDate.of(2024, 5, 1));
        return film;
    }

    private static Mpa getMpa(Long id, String name) {
        Mpa mpa = new Mpa();
        mpa.setId(id);
        mpa.setName(name);
        return mpa;
    }
}