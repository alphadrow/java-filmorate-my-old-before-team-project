package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements  FilmStorage{
    private final Set<Film> films = new HashSet<>();

    @Override
    public Set<Film> findAll() {
        return films;
    }

    @Override
    public Film create(Film film) {
        films.add(film);
        return film;
    }

    @Override
    public Film renew(Film film) throws FilmNotFoundException {
        removeById(film.getId());
        films.add(film);
        return film;
    }

    @Override
    public Optional<Film> getById(long id) {
        return films.stream().filter(film -> film.getId() == id).findAny();
    }

    protected void removeById(long id) throws FilmNotFoundException {
        try {
            films.remove(films.stream().filter(film -> film.getId() == id).findAny().get());
        } catch (NoSuchElementException e) {
            throw new FilmNotFoundException("Фильма с id " + id + "нет в базе.");
        }
    }
}
