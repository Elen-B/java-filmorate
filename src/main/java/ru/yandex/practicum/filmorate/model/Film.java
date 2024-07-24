package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterCinemaBirthday;

import java.time.LocalDate;
import java.util.*;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @AfterCinemaBirthday(
        message = "Дата выхода фильма слишком далеко в прошлом"
    )
    private LocalDate releaseDate;
    @Positive
    private Long duration;
    private Set<Long> userLikes = new HashSet<>();
    private Mpa mpa;
    private List<Genre> genres;
}