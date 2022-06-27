package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Set;

public interface FilmDao {

    Set<Film> get();

    Set<Film> getPopular(Integer size);

    Film getById(int id) throws FilmNotFoundException;

    Film create(Film film);

    Film update(Film updatedFilm) throws FilmNotFoundException;

    void like(Integer filmId, Integer userId) throws FilmNotFoundException, UserNotFoundException;

    void dislike(Integer filmId, Integer userId) throws FilmNotFoundException, UserNotFoundException;

    boolean isEmpty(int id);

    List<Mpa> getAllMpa();

    Mpa getMpaById(int id) throws MpaNotFoundException;

    List<Genres> getAllGenres();

    Genres getGenreById(int id) throws GenreNotFoundException;
}
