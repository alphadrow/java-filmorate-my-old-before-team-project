package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    public Set<Long> likes = null;
    private long id;
    @NotEmpty
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private int rate;

    public void like(Long id){
        if (likes == null){
            likes = new HashSet<>();
        }
        likes.add(id);
    }

    public void dislike(Long id){
        if (likes == null){
            likes = new HashSet<>();
        }
        if (likes.contains(id)) {
            likes.remove(id);
        } else {
            throw new LikeNotFoundException("Вы не ставили лайк этому фильму. Нечего удалять.");
        }
    }

    public Set<Long> getFriends(){
        return likes;
    }

}
