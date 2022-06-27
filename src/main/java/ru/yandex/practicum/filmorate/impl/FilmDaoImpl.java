package ru.yandex.practicum.filmorate.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.GenresMapper;
import ru.yandex.practicum.filmorate.utils.MpaMapper;
import ru.yandex.practicum.filmorate.utils.UserMapper;
import java.util.*;

@Slf4j
@Component
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Integer> getLikesByFilmId(int id) {

        String sql = "select user_id from likes where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id") , id);
    }
    @Override
    public Set<Film> get() {
        return new HashSet<>(jdbcTemplate.query("select * from films ORDER BY RATE DESC LIMIT(10)",
                new FilmMapper()));
    }

    @Override
    public Set<Film> getPopular(Integer size) {
        Set<Film> result = new HashSet<Film>(jdbcTemplate.query("select * from films ORDER BY RATE DESC LIMIT(?)",
                new FilmMapper(), size));
        for (Film film : result) {
            fillFilm(film, film.getId());
        }
        return result;
    }

    private void fillFilm(Film film, int id){
        film.setMpa(jdbcTemplate.query("select * from MPA where ID IN (SELECT MPA FROM FILMS WHERE ID = ?)",
                new MpaMapper(), id).stream().findAny().orElse(new Mpa()));
        film.setGenres(new HashSet<>(jdbcTemplate.query("SELECT g1.GENRE as ID, g2.name " +
                        "FROM GENRES g1 INNER JOIN GENRE_LIST g2 ON g1.GENRE=g2.ID WHERE FILM_ID = ? ORDER BY ID;",
                new GenresMapper(), id)));
        if (film.getGenres().isEmpty()) {
            film.setGenres(null);
        }
    }

    @Override
    public Film getById(int id) throws FilmNotFoundException {
        Film result = jdbcTemplate.query("select * from films where ID = ?",
                new FilmMapper(), id).stream().findAny().orElseThrow(() ->
                new FilmNotFoundException("Фильма с id "+ id + " нет в базе"));
        fillFilm(result, id);
        return result;
    }

    @Override
    public Film create(Film film) {


        jdbcTemplate.update("INSERT INTO FILMS (NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATE, MPA)" +
                        "VALUES (?, ?, ?, ?, ?, ?);",
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId());

        int id = jdbcTemplate.query("SELECT MAX(ID) FROM FILMS as ID",
                (rs, rowNum) -> rs.getInt(1)).stream().findAny().orElse(0);

        if (film.getGenres() != null){
            for (Genres genre: film.getGenres()) {
                jdbcTemplate.update(   "INSERT INTO GENRES (FILM_ID, GENRE) VALUES (?, ?);", id, genre.getId());
            }
        }
        film.setId(id);

        return film;
    }

    @Override
    public Film update(Film updatedFilm) throws FilmNotFoundException {
        Film result = new Film();
        log.debug("updateFilm case: updatedFilm.toString() {}", updatedFilm.toString());
        jdbcTemplate.update("UPDATE FILMS SET " +
                "NAME = ?, RELEASE_DATE = ?, DESCRIPTION = ?, DURATION = ?, RATE = ?, MPA = ?" +
                        "WHERE ID = ?;",
                updatedFilm.getName(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDescription(),
                updatedFilm.getDuration(),
                updatedFilm.getRate(),
                updatedFilm.getMpa().getId(),
                updatedFilm.getId());
        if (updatedFilm.getMpa() != null){
            jdbcTemplate.update(   "UPDATE MPA SET ID = ?, NAME = ? " +
                    "WHERE NAME = ?; ", updatedFilm.getMpa().getId(), updatedFilm.getMpa().getName(),
                    updatedFilm.getMpa().getName());
        }
        log.debug("updatedFilm.getGenres() {}", updatedFilm.getGenres());
        log.debug("updatedFilm.getGenres() = null? {}", (updatedFilm.getGenres() == null));
        if (updatedFilm.getGenres() != null) {
            jdbcTemplate.update("DELETE FROM GENRES WHERE FILM_ID = ?", updatedFilm.getId());
            if (!updatedFilm.getGenres().isEmpty()) {
                int count = 1;
                for (Genres genre : updatedFilm.getGenres()) {
                    log.debug("genre.getName() = {}, genre.getId() = {}", genre.getName(), genre.getId());
                    jdbcTemplate.update("MERGE INTO GENRES VALUES (?, ?, ?)", count
                            ,updatedFilm.getId(), genre.getId());
                    count++;
                }
            }
        }
        result = getById(updatedFilm.getId());
        if (updatedFilm.getGenres() != null) {
            if (updatedFilm.getGenres().isEmpty()) {
                result.setGenres(updatedFilm.getGenres());
            }
        }
        return  result;
    }


    @Override
    public void dislike(Integer filmId, Integer userId) throws FilmNotFoundException, UserNotFoundException {
        if (isEmpty(filmId)){
            throw new FilmNotFoundException("Фильма с id " + filmId + "нет в базе");
        }
        if (checkForUserExist(userId)) {
            throw new UserNotFoundException("Пользователя с id " + filmId + "нет в базе");
        }
        jdbcTemplate.update("DELETE FROM LIKES WHERE FILM_ID=? AND USER_ID = ?;" +
                "update FILMS SET RATE = RATE - 1 WHERE ID = ?", filmId, userId, filmId);
    }

    @Override
    public boolean isEmpty(int id) {
        return jdbcTemplate.query("select * from films where ID = ?",
                new FilmMapper(), id).stream().findAny().isEmpty();
    }

    @Override
    public List<Mpa> getAllMpa() {
        return new LinkedList<>(jdbcTemplate.query("select * from MPA ORDER BY ID",
                new MpaMapper()));
    }

    @Override
    public Mpa getMpaById(int id) throws MpaNotFoundException {
        if (checkForMpaExist(id)){
            throw new MpaNotFoundException("Рейтинга с id " + id + "нет в базе!");
        }
        return jdbcTemplate.queryForObject("select * from MPA WHERE ID = ?", new MpaMapper(), id);
    }

    @Override
    public List<Genres> getAllGenres() {
        return new LinkedList<Genres>(jdbcTemplate.query("select * from GENRE_LIST ORDER BY ID",
                new GenresMapper()));
    }

    @Override
    public Genres getGenreById(int id) throws GenreNotFoundException {
        log.debug("getGenreById started");
        if (checkForGenreExist(id)){
            throw new GenreNotFoundException("Жанра с id " + id + "нет в базе!");
        }
        return jdbcTemplate.queryForObject("select * from GENRE_LIST WHERE ID = ?", new GenresMapper(), id);
    }

    public boolean checkForMpaExist(int id) {
        log.debug("checkForMpaExist started, id = {}", id);
        return jdbcTemplate.query("select * from GENRE_LIST where ID = ?",
                new MpaMapper(), id).stream().findAny().isEmpty();
    }

    public boolean checkForGenreExist(int id) {
        return jdbcTemplate.query("select * from GENRE_LIST where ID = ?",
                new GenresMapper(), id).stream().findAny().isEmpty();
    }

    public boolean checkForUserExist(int id) {
        return jdbcTemplate.query("select * from USERS where ID = ?",
                new UserMapper(), id).stream().findAny().isEmpty();
    }

    @Override
    public void like(Integer filmId, Integer userId) throws FilmNotFoundException, UserNotFoundException {
        if (isEmpty(filmId)){
            throw new FilmNotFoundException("Фильма с id " + filmId + "нет в базе");
        }
        if (checkForUserExist(userId)) {
            throw new UserNotFoundException("Пользователя с id " + filmId + "нет в базе");
        }
            jdbcTemplate.update("INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?);" +
                    "UPDATE FILMS SET RATE = RATE + 1 WHERE ID = ?", filmId, userId, filmId);
    }

}
