package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.Collection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate DATE = LocalDate.of(1990, 8, 25);
    private int filmId = 1;
    final Map<Integer, Film> filmsList = new HashMap<>();

    @GetMapping
    public Collection<Film> findAllFilms() {

        return filmsList.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(filmId++);
        filmsList.put(film.getId(), film);
        log.info("Фильм {} добавлен в коллекцию", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        validate(film);
        if (!filmsList.containsKey(film.getId())) throw new ValidationException("Такого фильма нет");
        filmsList.remove(film.getId());
        filmsList.put(film.getId(), film);
        log.info("Информация о фильме была {} обновлена", film.getName());
        return film;
    }

    void validate(@Valid @RequestBody Film film) {
        if (film.getDuration() < 0 ||  film.getReleaseDate().isBefore(DATE) ) {
            log.warn("film.getReleaseDate film release date: '{}'\n film.getDuration film duration: {}", film.getReleaseDate(), film.getDuration());
            throw new ValidationException("В указанное время фильма нет, или продолжительность указана неверно");
        }
        Collection<Film> filmCollection = filmsList.values();
        for (Film fl : filmCollection) {
            if (film.getName().equals(fl.getName()) && film.getReleaseDate().equals(fl.getReleaseDate())) {
                log.warn("film film: '{}'\n fl film: {}", film, fl);
                throw new ValidationException("Такой фильм уже есть");
            }
        }
    }
}