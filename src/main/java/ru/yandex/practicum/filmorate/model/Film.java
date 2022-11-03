package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;


import java.time.LocalDate;

@Data
public class Film {

    private int id;
    @NotNull

    @NotBlank
    private final String name;

    @Size(max = 200, message = "слишком длинное описание, недопустимое значение.")
    private final String description;

    @NotNull
    private final LocalDate releaseDate;
    private final long duration;







}