package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LikeNotFoundException extends Exception{

    public LikeNotFoundException(String s){
        super(s);
    }
}
