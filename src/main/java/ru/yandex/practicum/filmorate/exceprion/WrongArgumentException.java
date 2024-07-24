package ru.yandex.practicum.filmorate.exceprion;

public class WrongArgumentException extends RuntimeException {
    public WrongArgumentException(String message) {
        super(message);
    }
}