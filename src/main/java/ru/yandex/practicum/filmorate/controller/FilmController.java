package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Set<Film> films = new HashSet<>();

    public boolean validate(Film film){
        LocalDate firstFilmDate = LocalDate.of(1895, 12, 28);
        log.debug("firstFilmDate={}",firstFilmDate);
        log.debug("film.getReleaseDate()={}", film.getReleaseDate());
        log.debug("film.getDescription().length(): {}", film.getDescription().length());
        if (film.getName() == null)  {
            log.debug("film.getName().isEmpty(): {}", film.getName());
            return false;
        }
        if (film.getName().isBlank())  {
            log.debug("film.getName(): {}", film.getName());
            return false;
        }
        if ((film.getDescription().length() > 200) || (film.getDescription().isBlank())) {
            log.debug("film.getDescription().length(): {}", film.getDescription().length());
            return false;
        }
        if (film.getDuration() < 0) {
            log.debug("film.getDuration().isNegative(): {}", film.getDuration() < 0);
            return false;
        }
        if (film.getDuration() == 0) {
            log.debug("film.getDuration().isZero(): {}", film.getDuration() == 0);
        return false;
        }
        if (film.getReleaseDate().isBefore(firstFilmDate)) {
            log.debug("film.getReleaseDate().isBefore(firstFilmDate): {}",
                    film.getReleaseDate().isBefore(firstFilmDate));
            return false;
        }
        return true;
    }

    @GetMapping
    public Set<Film> findAll() {
        return films;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос на добавление фильма: {}",
                film);
        if (validate(film)) {
            films.add(film);
            log.debug("film {} was added", film);
            return film;
        } else {
            log.warn("Валидация фильма не пройдена!");
            throw new ValidationException("Неверные параметры фильма!");
        }
    }

    @PutMapping
    public void renew(@RequestBody Film film){

        if (films.stream().anyMatch(t -> t.getName().equals(film.getName()))) {
            films.remove(film);
        }
//        create(film);
        films.add(film);
    }
}
