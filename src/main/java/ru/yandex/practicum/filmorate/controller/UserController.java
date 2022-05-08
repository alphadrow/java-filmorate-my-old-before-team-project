package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j

@RestController
@RequestMapping("/users")
public class UserController {

    public boolean validate(User user){
        if (user.getEmail() == null){
            log.debug("user.getEmail(): {}", user.getEmail());
            return false;
        }
        if (!user.getEmail().contains("@")) {
            log.debug("!user.getEmail().contains(\"@\"): {}", !user.getEmail().contains("@"));
            return false;
        }
        if (user.getEmail().isBlank()) {
            log.debug("user.getEmail().isBlank(): {}", user.getEmail().isBlank());
            return false;
        }
        if (user.getLogin() == null) {
            log.debug("user.getLogin() == null");
            return false;
        }
        if (user.getLogin().isBlank()) {
            log.debug("user.getLogin().isBlank(): {}", user.getLogin().isBlank());
            return false;
        }
        if (user.getLogin().contains(" ")) {
            log.debug("user.getLogin().contains(\" \"): {}", user.getLogin().contains(" "));
            return false;
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("user birthday is after today");
            return false;
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.debug("setName trigger");
            user.setName(user.getLogin());
        }
    return true;
    }
    private final Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> findAll() {
        return users;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос на добавление пользователя: {}",
                user);
        if (validate(user)) {
            log.debug("user: {}", user);
            users.add(user);
            return user;
        } else {
            log.warn("Валидация пользователя не пройдена!");
            throw new ValidationException("Неверные параметры пользователя!");
        }
    }

    @PutMapping
    public void renew(@RequestBody User user){

        if (users.stream().anyMatch(t -> t.equals(user))) {
            users.remove(user);
        }
//        create(user);
        users.add(user);
    }
}

