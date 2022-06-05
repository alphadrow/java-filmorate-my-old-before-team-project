package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    public FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Film getFilmById(@PathVariable int id) throws FilmNotFoundException {
            return filmService.getById(id);
    }


    @GetMapping("/popular")
    @ResponseBody
    public Set<Film> getTopRatedFilms(@RequestParam(required = false) String count){
        if (count == null) {
            return filmService.getTopByLikes(10);
        }
        return filmService.getTopByLikes(Integer.parseInt(count));
    }

    @GetMapping
    @ResponseBody
    public Set<Film> findAll() {
        return filmService.findAll();
    }



    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
            filmService.create(film);
            return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable int id, @PathVariable long userId){
        filmService.like(id, userId);
    }

    @PutMapping
    @ResponseBody
    public Film renew(@RequestBody Film film) throws FilmNotFoundException {
        return (filmService.renew(film));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislike(@PathVariable long id, @PathVariable long userId) throws FilmNotFoundException, LikeNotFoundException {
        filmService.dislike(id, userId);
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
}
