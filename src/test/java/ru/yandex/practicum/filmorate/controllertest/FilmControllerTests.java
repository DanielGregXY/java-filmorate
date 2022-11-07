package ru.yandex.practicum.filmorate.controllertest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTests {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    private Validator validator = factory.getValidator();

    FilmController filmController;
    Film film;

    @BeforeEach
    void filmControllerInit() {
        filmController = new FilmController();
    }

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        filmController = new FilmController();
    }

    @Test
    void releaseDateBefore1895() {
        film = new Film("Фильм", "Какое-то описание", LocalDate.of(1894, 1, 2), 60);

        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void duplicateFilmTest() {
        film = new Film("Фильм", "Какое-то описание", LocalDate.of(1995, 12, 29), 60);
        film.setId(1);
        filmController.films.put(film.getId(), film);
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void emptyNameValidationTest() {
        film = new Film("", "Какое-то описание", LocalDate.of(1894, 1, 2), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void blancValidationTest() {
        film = new Film(" ", "Какое-то описание", LocalDate.of(1894, 1, 2), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void nullNameValidationTest() {
        film = new Film(null, "Какое-то описание", LocalDate.of(1894, 1, 2), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void blancDescriptionTest() {
        film = new Film("Фильм", " ", LocalDate.of(1995, 12, 29), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void emptyDescriptionTest() {
        film = new Film("Film", "", LocalDate.of(2021, 1, 2), 120);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void nullDescriptionTest() {
        film = new Film("Фильм", null, LocalDate.of(1995, 12, 29), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void lengthDescriptionAbove200Test() {
        film = new Film("Название", "In 20 years, you'll be creating more frustration with those things," +
                "which you didn't do than those you did." +
                "So move away from the quiet harbor. Feel the tailwind in your sail" +
                "Move forward, act, open." +
                "It is often said that motivation does not last long”, + “But the same thing happens with a refreshing shower, which is why it is recommended to take it daily.",
                LocalDate.of(1995, 10, 13), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void durationNotNullTest() {
        film = new Film("Фильм", "описание", LocalDate.of(1995, 12, 29), 60);
        Set<ConstraintViolation<Film>> violations= validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

}