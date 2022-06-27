package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

 //   private Set<Integer> friends = new HashSet<>();
    private int id;
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

    public User(int user_id, String name, String login, String email, LocalDate birthday) {
        this.id = user_id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }
/*
    public void addToFriends(Long id){
        friends.add(id);
    }



    public void removeFromFriends(Long id){
        friends.remove(id);
    }

    public Set<Integer> getFriends(){
        return friends;
    }

 */
}
