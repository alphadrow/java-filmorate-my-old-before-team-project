package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {

 //   private Set<Integer> likes = null;
    private Mpa mpa;
    private Set<Genres> genres;
    private int id;
    @NotEmpty
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private int rate;


    public Film() {
    }

}
