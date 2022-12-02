package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class User {

        private Set<Integer> friends = new HashSet<>();

                private int id;
                @NotBlank(message = "Отсутствует email")
                @Email(message = "Некорректный email")

                private String email;
                @NotNull(message = "Отсутствует логин")
                @Pattern(regexp = "\\S+", message = "Логин содержит пробелы")
                private final String login;

                private String name;
                @NotNull(message = "Не указана дата рождения")
                @PastOrPresent(message = "Некорректная дата рождения")
                private final LocalDate birthday;

                private StatusRelation statusRelation;

        }