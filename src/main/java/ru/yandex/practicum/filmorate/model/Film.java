package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.AfterCinemaBirthday;
import ru.yandex.practicum.filmorate.validator.PositiveDuration;

import java.time.Duration;
import java.time.Instant;

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
    @AfterCinemaBirthday
    private Instant releaseDate;
    @PositiveDuration
    private Duration duration;
}