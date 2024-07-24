package ru.yandex.practicum.filmorate.exceprion;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}