package ru.yandex.practicum.filmorate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;

public class FilmControllerTest {
    public static void main(String[] args) {

    }
    FilmController filmController = new FilmController();
    Film film;

    @BeforeEach
    public void beforeEach() {

        film = new Film();
        film.setName("Normal name");
        film.setDescription("Normal description");
        film.setId(1);
        film.setReleaseDate(LocalDate.of(1990, 02, 22));
        film.setDuration(1);
    }

    @Test
    public void ShouldTrue() {

        System.out.println("film = " + film);
    assertTrue(filmController.validate(film));
    }

    @Test
    public void ShouldFalseIfNameIsBlankOrNull() {

        film.setName("");
        assertFalse(filmController.validate(film));
        film.setName(null);
        assertFalse(filmController.validate(film));
    }

    @Test
    public void ShouldFalseIfDecrLIsMoreThan200() {
        StringBuilder sb = new StringBuilder();
        sb.append("a".repeat(199));
        String result = sb.toString();
        film.setDescription(result);
        assertTrue(filmController.validate(film));
        film.setDescription(result += " ");
        assertTrue(filmController.validate(film));
        film.setDescription(result += " ");
        assertFalse(filmController.validate(film));
    }

    @Test
    public void ShouldFalseIfReleaseDateIsOlderThan18951228(){

        film.setReleaseDate(LocalDate.of(1895,12,27));
        assertFalse(filmController.validate(film));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertTrue(filmController.validate(film));
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        assertTrue(filmController.validate(film));
        film.setReleaseDate(LocalDate.MAX);
        assertTrue(filmController.validate(film));
        film.setReleaseDate(LocalDate.MIN);
        assertFalse(filmController.validate(film));
    }

    @Test
    public void ShouldFalseIfDurationLessOrEqualsZero(){

        film.setDuration(0);
        assertFalse(filmController.validate(film));
        film.setDuration(1);
        assertTrue(filmController.validate(film));
        film.setDuration(-1);
        assertFalse(filmController.validate(film));
    }

}
