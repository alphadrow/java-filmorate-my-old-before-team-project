package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@Data
public class User {

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
}
