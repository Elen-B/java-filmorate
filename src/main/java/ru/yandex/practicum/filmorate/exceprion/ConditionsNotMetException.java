package ru.yandex.practicum.filmorate.exceprion;

public class ConditionsNotMetException extends RuntimeException {
    public ConditionsNotMetException(String message) {
        super(message);
    }
}
