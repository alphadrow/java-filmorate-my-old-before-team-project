package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class FilmController {

    public FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films/{id}")
    @ResponseBody
    public Film getFilmById(@PathVariable int id) throws FilmNotFoundException {
            return filmService.getById(id);
    }


    @GetMapping("/films/popular")
    @ResponseBody
    public Set<Film> getTopRatedFilms(@RequestParam(required = false) String count){
        if (count == null) {
            return filmService.getTopByLikes(10);
        }
        return filmService.getTopByLikes(Integer.parseInt(count));
    }

    @GetMapping("/films")
    @ResponseBody
    public Set<Film> findAll() {
        return filmService.findAll();
    }



    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
            return filmService.create(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void like(@PathVariable int id, @PathVariable int userId) throws FilmNotFoundException, UserNotFoundException {
        filmService.like(id, userId);
    }

    @PutMapping("/films")
    @ResponseBody
    public Film renew(@RequestBody Film film) throws FilmNotFoundException {
        log.debug("/film case, film.toString() {}", film.toString());
        return (filmService.renew(film));
    }


    @DeleteMapping("/films/{id}/like/{userId}")
    public void dislike(@PathVariable int id, @PathVariable int userId) throws FilmNotFoundException, LikeNotFoundException, UserNotFoundException {
        filmService.dislike(id, userId);
    }

    @GetMapping("/mpa")
    @ResponseBody
    public List<Mpa> getAllMpa(){
        return filmService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    @ResponseBody
    public Mpa getMpaById(@PathVariable int id) throws MpaNotFoundException {
        return filmService.getMpaById(id);
    }

    @GetMapping("/genres")
    @ResponseBody
    public List<Genres> getAllGenres(){
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    @ResponseBody
    public Genres getGenreById(@PathVariable int id) throws GenreNotFoundException {
        log.debug("getGenreById top");
        return filmService.getGenreById(id);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> HandleFilmNotFoundException(final FilmNotFoundException e) {
        return new ResponseEntity<>(
                Map.of("error", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> HandleLikeNotFoundException(final LikeNotFoundException e ) {
        return new ResponseEntity<>(
                Map.of("error", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> HandleMpaNotFoundException(final MpaNotFoundException e ) {
        return new ResponseEntity<>(
                Map.of("error", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> HandleGenreNotFoundException(final GenreNotFoundException e ) {
        return new ResponseEntity<>(
                Map.of("error", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
