package ru.yandex.practicum.filmorate.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.utils.GenresMapper;

import java.util.HashSet;
import java.util.Set;

@Component
public class GenresDaoImpl implements GenresDao {

    private final JdbcTemplate jdbcTemplate;
    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Genres> getGenresByFilmId(int id) {
        return new HashSet<>(jdbcTemplate.query("select * from GENRES",
                new GenresMapper()));
    }
}
