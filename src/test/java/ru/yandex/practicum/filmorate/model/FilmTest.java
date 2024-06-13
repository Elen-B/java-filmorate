package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {

    @Test
    void shouldCreateFilm() {
        Long id = 1L;
        String name = "фильм 1";
        String description = "описание фильма";
        LocalDate releaseDate = LocalDate.of(2020, Month.MARCH, 1);
        Long duration = 120L;
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);

        assertNotNull(film, "Экзмепляр фильма не создан");
        assertEquals(name, film.getName(), "Неверное наименование созданного фильма");
        assertEquals(description, film.getDescription(), "Неверное описание созданного фильма");
        assertEquals(releaseDate, film.getReleaseDate(), "Неверная дата выпуска созданного фильма");
        assertEquals(duration, film.getDuration(), "Неверная длительность созданного фильма");
    }

    @Test
    void shouldNotValidFilmWithEmptyName() {
        Film film = buildValidTestFilm();
        film.setName(null);

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var violations = validator.validate(film);
            assertEquals(1, violations.stream()
                    .toList().size(), "Валидация не выполнена");
            assertEquals("NotBlank",
                    violations.stream()
                            .toList()
                            .getFirst()
                            .getConstraintDescriptor()
                            .getAnnotation().annotationType().getSimpleName(),
                    "Не работает валидация на пустое наименование фильма");
        }
    }

    @Test
    void shouldNotValidFilmWithLargeSizeDescription() {
        Film film = buildValidTestFilm();
        film.setDescription("a".repeat(201));

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var violations = validator.validate(film);
            assertEquals(1, violations.stream()
                    .toList().size(), "Валидация не выполнена");
            assertEquals("Size",
                    violations.stream()
                            .toList()
                            .getFirst()
                            .getConstraintDescriptor()
                            .getAnnotation().annotationType().getSimpleName(),
                    "Не работает валидация на длинное описание фильма");
        }
    }

    @Test
    void shouldNotValidFilmWithWrongReleaseDate() {
        Film film = buildValidTestFilm();
        film.setReleaseDate(LocalDate.of(1800, Month.DECEMBER, 1));

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var violations = validator.validate(film);
            assertEquals(1, violations.stream()
                    .toList().size(), "Валидация не выполнена");
            assertEquals("AfterCinemaBirthday",
                    violations.stream()
                            .toList()
                            .getFirst()
                            .getConstraintDescriptor()
                            .getAnnotation().annotationType().getSimpleName(),
                    "Не работает валидация дату создания фильма");
        }
    }

    @Test
    void shouldNotValidFilmWithNegativeDuration() {
        Film film = buildValidTestFilm();
        film.setDuration(-5L);

        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var violations = validator.validate(film);
            assertEquals(1, violations.stream()
                    .toList().size(), "Валидация не выполнена");
            assertEquals("Positive",
                    violations.stream()
                            .toList()
                            .getFirst()
                            .getConstraintDescriptor()
                            .getAnnotation().annotationType().getSimpleName(),
                    "Не работает валидация положительное значение длительности фильма");
        }
    }

    private Film buildValidTestFilm() {
        Long id = 1L;
        String name = "фильм 1";
        String description = "описание фильма";
        LocalDate releaseDate = LocalDate.of(2020, Month.MARCH, 1);
        Long duration = 120L;
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        return film;
    }
}