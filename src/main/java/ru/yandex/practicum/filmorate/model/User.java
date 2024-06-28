package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @Email
    @NotNull(
            message = "Не может быть пустым"
    )
    private String email;
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent(
            message = "Неверная дата рождения"
    )
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            this.name = this.login;
        } else {
            this.name = name;
        }
    }
}