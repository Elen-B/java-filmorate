package ru.yandex.practicum.filmorate.exceprion;

public class DublicateException extends RuntimeException {
    public DublicateException(String message) {
        super(message);
    }
}