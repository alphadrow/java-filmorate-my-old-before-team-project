package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Set<Long> friends = new HashSet<>();
    private long id;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    @NotBlank

    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public User() {
    }

    public void addToFriends(Long id){
        friends.add(id);
    }

    public void removeFromFriends(Long id){
        friends.remove(id);
    }

    public Set<Long> getFriends(){
        return friends;
    }
}
