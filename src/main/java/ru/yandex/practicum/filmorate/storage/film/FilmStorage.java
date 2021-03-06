package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    public Set<Film> findAll();
    public Film create(Film film);
    public Film renew(Film film) throws FilmNotFoundException;

    public Optional<Film> getById(long id);
    }
