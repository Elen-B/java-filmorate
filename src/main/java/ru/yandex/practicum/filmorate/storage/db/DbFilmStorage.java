package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class DbFilmStorage extends DbBaseStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT f.*, " +
            "(SELECT Listagg(fg.genre_id, ',') FROM film_genre fg WHERE fg.film_id = f.film_id) as genres, " +
            "(SELECT Listagg(fl.user_id, ',') FROM film_like fl WHERE fl.film_id = f.film_id) as likes FROM film f";

    private static final String FIND_BY_ID_QUERY = "SELECT f.*, " +
            "(SELECT Listagg(fg.genre_id, ',') FROM film_genre fg WHERE fg.film_id = f.film_id) as genres, " +
            "(SELECT Listagg(fl.user_id, ',') FROM film_like fl WHERE fl.film_id = f.film_id) as likes " +
            "FROM film f WHERE f.film_id = ?";

    private static final String INSERT_QUERY = "INSERT INTO film(name, description, release_date, duration, rating_id)" +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE film set name = ?, description = ?, release_date = ?, " +
            "duration = ?, rating_id = ? WHERE film_id = ?";

    private static final String DELETE_QUERY = "DELETE FROM film where film_id = ?";

    private static final String DELETE_GENRE_QUERY = "DELETE FROM film_genre fg where fg.film_id = ?";

    private static final String ADD_GENRE_QUERY = "MERGE INTO film_genre KEY(film_id, genre_id) VALUES(?, ?)";

    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_like fl where fl.film_id = ? AND fl.user_id = ?";

    private static final String ADD_LIKE_QUERY = "MERGE INTO film_like KEY(film_id, user_id) VALUES(?, ?)";

    public DbFilmStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public Collection<Film> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film getById(Long id) {
        Optional<Film> film = findOne(FIND_BY_ID_QUERY, id);
        return film.orElse(null);
    }

    @Override
    public Film add(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );
        setGenres(id, getGenresId(film.getGenres()));
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        setGenres(film.getId(), getGenresId(film.getGenres()));
        return film;
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public void addLike(Film film, User user) {
        update(
                ADD_LIKE_QUERY,
                film.getId(),
                user.getId()
        );
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        delete(
                DELETE_LIKE_QUERY,
                filmId,
                userId
        );
    }

    private void setGenres(Long filmId, List<Long> genres) {
        delete(DELETE_GENRE_QUERY, filmId);
        if (genres != null && !genres.isEmpty()) {
            genres.forEach(genreId -> update(
                    ADD_GENRE_QUERY,
                    filmId,
                    genreId
            ));
        }
    }

    private List<Long> getGenresId(List<Genre> genres) {
        return genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
    }

}
