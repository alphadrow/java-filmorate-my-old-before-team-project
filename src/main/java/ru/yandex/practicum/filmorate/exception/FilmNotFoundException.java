package ru.yandex.practicum.filmorate.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
Существует ли какой-то способ обработать исключения средствами Spring'а не пробрасывая их на самый верх?
И нужны ли вообще эти исключения, если получается что это штатная ситуация в работе программы?
У меня такое чувство что я делаю что-то не так :)
 */

@ResponseStatus(code = HttpStatus.NOT_FOUND)        // вот эта строка по-моему вообще ничего не меняет :)
public class FilmNotFoundException extends Exception{
    public FilmNotFoundException(String s){
        super(s);
    }
}
