package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserControllerTest {

    UserController userController;
    User user;


    @BeforeEach
    public void beforeEach() {
        userController = new UserController(new UserService(new UserDaoImpl(new JdbcTemplate())) {
        });
        user = new User();
        user.setName("User");
        user.setId(1);
        user.setEmail("valid@mail.com");
        user.setBirthday(LocalDate.of(1956, 05, 12));
        user.setLogin("login");
    }

    @Test
    public void ShouldTrue() {
        assertTrue(userController.userService.validate(user));
    }

    @Test
    public void shouldFalseIfEmailIsEmptyOrAtIsMissed(){

        user.setEmail("");
        assertFalse(userController.userService.validate(user));
        user.setEmail(null);
        assertFalse(userController.userService.validate(user));
        user.setEmail("wrong-email.com");
        assertFalse(userController.userService.validate(user));
    }

    @Test
    public void shouldFalseIfLoginContainsSpacesOrBlank() {

        user.setLogin("");
        assertFalse(userController.userService.validate(user));
        user.setLogin(" asdad");
        assertFalse(userController.userService.validate(user));
        user.setLogin(null);
        assertFalse(userController.userService.validate(user));
    }

    @Test
    public void shouldFillNameWithLoginIfNameIsBlank() {

        user.setName("");
        log.debug("user before validation: {}", user);
        userController.userService.validate(user);
        log.debug("user after validation: {}", user);
        assertEquals(user.getLogin(), user.getName());
        user.setName(null);
        userController.userService.validate(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void shouldFalseIfBirthdayIsInFuture() {

        user.setBirthday(LocalDate.now().plusDays(1));
        assertFalse(userController.userService.validate(user));
    }

}
