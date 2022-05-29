package ru.yandex.practicum.filmorate.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.management.StringValueExp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;
    long id;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void like(long filmId, long userId){
        filmStorage.getById(filmId).ifPresentOrElse(film -> {
                    film.like(userId);
            try {
                filmStorage.renew(film);
            } catch (FilmNotFoundException e) {
                throw new RuntimeException(e);
            }
            log.debug(film.getLikes().toString());
                    log.debug(filmStorage.getById(filmId).get().getLikes().toString());
                    },() -> {
            try {
                throw new FilmNotFoundException("Фильма с ID " + filmId + " нет в базе");
            } catch (FilmNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void dislike(long filmId, long userId) throws LikeNotFoundException, FilmNotFoundException {
        Optional<Film> film = filmStorage.getById(filmId);
        if (film.isPresent()) {
            Film sub = film.get();
            sub.dislike(userId);
            filmStorage.renew(sub);
        } else {
            throw new FilmNotFoundException("Фильма с ID " + filmId + " нет в базе");
        }
    }

    public Set<Film> getTopByLikes(int size){
        log.debug("size: " + size);
        log.debug("filmstorage size: " + filmStorage.findAll().size());
        return filmStorage.findAll()
                .stream()
                .sorted(filmByLikesComparator)
                .limit(size)
                .collect(Collectors.toSet());
        }

    public boolean validate(Film film){
        LocalDate firstFilmDate = LocalDate.of(1895, 12, 28);
        log.debug("firstFilmDate={}",firstFilmDate);
        log.debug("film.getReleaseDate()={}", film.getReleaseDate());
        log.debug("film.getDescription().length(): {}", film.getDescription().length());
        if (!StringUtils.hasText(film.getName()))  {
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


    public Set<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getById(int id) throws FilmNotFoundException {

        if (filmStorage.getById(id).isPresent()) {
            return filmStorage.getById(id).get();
        } else {
            throw new FilmNotFoundException("Фильма с айди" + id + "нет в базе.");
        }
    }

    public Film create(Film film) {
        log.debug("Получен запрос на добавление фильма: {}",
                film);
        if (validate(film)) {
            film.setId(++id);
            filmStorage.create(film);
            log.debug("film {} was added", film);
            return film;
        } else {
            log.warn("Валидация фильма не пройдена!");
            throw new ValidationException("Неверные параметры фильма!");
        }
    }

    public Film renew(Film film) throws FilmNotFoundException {
        return filmStorage.renew(film);
    }

    public Comparator<Film> filmByLikesComparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            if (o1.likes == null) {
                if (o2.likes == null){
                    return 0;
                } else return 1;
            } else if (o2.likes == null) {
                return -1;
            }
            return o1.likes.size() - o2.likes.size();
        }
    };
}
