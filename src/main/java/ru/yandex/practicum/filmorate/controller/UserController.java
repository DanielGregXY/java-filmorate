package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private int userId = 1;
     public final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь с логином {}", user.getLogin());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        validate(user);
        if (!users.containsKey(user.getId()))
            throw new ValidationException("Пользователя не существует, зарегистрируйте новый аккаунт");
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Информация о пользователе {} обновлена", user.getLogin());
        return user;
    }

    public void validate(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя '{}'", user.getLogin());
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        } if (user.getName().isBlank() || user.getName() == null ) user.setName(user.getLogin());
        Collection<User> userCollection = users.values();
        for (User us : userCollection) {
            if (user.getEmail().equals(us.getEmail()) || user.getLogin().equals(us.getLogin()) ) {
                log.warn("user e-mail: '{}'\n us email: {}", user, us);
                throw new ValidationException("Пользователь с таким данными уже существует");
            }
        }
    }
}