package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.Set;

public interface GenresDao {

    Set<Genres> getGenresByFilmId(int id);
}
