package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmDao {

    Set<Film> getFilms();

    Set<Film> getPopularFilms(Integer size);

    Film getFilmById(int id) throws FilmNotFoundException;

    Film createFilm(Film film);

    Film updateFilm(Film updatedFilm) throws FilmNotFoundException;

    void like(Integer filmId, Integer userId) throws FilmNotFoundException, UserNotFoundException;

    Set<Integer> getLikesByFilmId(int id);

    void dislike(Integer filmId, Integer userId) throws FilmNotFoundException, UserNotFoundException;

    boolean checkForFilmExist(int id);

    List<Mpa> getAllMpa();

    Mpa getMpaById(int id) throws MpaNotFoundException;

    List<Genres> getAllGenres();

    Genres getGenreById(int id) throws GenreNotFoundException;
}
