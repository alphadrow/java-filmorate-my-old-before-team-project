package ru.yandex.practicum.filmorate.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND)        // вот эта строка по-моему вообще ничего не меняет :)
public class FilmNotFoundException extends Exception{
    public FilmNotFoundException(String s){
        super(s);
    }
}
