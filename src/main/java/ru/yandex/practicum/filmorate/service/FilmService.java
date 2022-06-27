package ru.yandex.practicum.filmorate.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FilmService {

    FilmDao filmDao;

    public FilmService(FilmDao filmDao) {
        this.filmDao = filmDao;
    }


    public void like(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        if (filmDao.getById(filmId) != null) {
            filmDao.like(filmId, userId);
        } else {
            try {
                throw new FilmNotFoundException("Фильма с ID " + filmId + " нет в базе");
            } catch (FilmNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dislike(int filmId, int userId) throws FilmNotFoundException, UserNotFoundException {
        if (filmDao.getById(filmId) != null) {
            filmDao.dislike(filmId, userId);
        } else {
            try {
                throw new FilmNotFoundException("Фильма с ID " + filmId + " нет в базе");
            } catch (FilmNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Set<Film> getTopByLikes(int size){
            return filmDao.getPopular(size);
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
        return filmDao.get();
    }

    public Film getById(int id) throws FilmNotFoundException {
        Film film = filmDao.getById(id);
        if (film == null) {
            throw new FilmNotFoundException("Фильма с айди" + id + "нет в базе.");
        }
        return film;
    }

    public Film create(Film film) {
        log.debug("Получен запрос на добавление фильма: {}",
                film);
        if (validate(film)) {
            return filmDao.create(film);
        } else {
            log.warn("Валидация фильма не пройдена!");
            throw new ValidationException("Неверные параметры фильма!");
        }
    }

    public Film renew(Film film) throws FilmNotFoundException {
        log.debug("renew case, film.toString() {}", film.toString());
        checkForFilmExist(film.getId());
        return filmDao.update(film);
    }


    private void checkForFilmExist(int id) throws FilmNotFoundException {
        if (filmDao.isEmpty(id)) {
            throw new FilmNotFoundException("Фильма с ID " + id + " нет в базе.");
        }
    }

    public List<Mpa> getAllMpa() {
        return filmDao.getAllMpa();
    }

    public Mpa getMpaById(int id) throws MpaNotFoundException {
        return filmDao.getMpaById(id);
    }

    public List<Genres> getAllGenres() {
        return filmDao.getAllGenres();
    }

    public Genres getGenreById(int id) throws GenreNotFoundException {
        log.debug("Получен запрос на получение жанра по id {}", id);
        return filmDao.getGenreById(id);
    }
}
