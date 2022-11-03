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
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private int filmId = 1;
    private final Map<Integer, Film> filmsList = new HashMap<>();

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

    private void validate(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(DATE) || film.getDuration() < 0) {
            log.warn("film.getReleaseDate film release date: '{}'\n film.getDuration film duration: {}", film.getReleaseDate(), film.getDuration());
            throw new ValidationException("В указанное время кино нет, или продолжительность неверная");
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