package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    public UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping()
    @ResponseBody
    public Set<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUserById(@PathVariable int id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    @ResponseBody
    public Set<User> getFriends(@PathVariable int id) throws UserNotFoundException {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseBody
    public Set<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) throws UserNotFoundException {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
            userService.create(user);
            return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseBody
    public void addToFriends(@PathVariable int id, @PathVariable int friendId) throws UserNotFoundException {
        userService.addToFriends(id, friendId);
    }

    @PutMapping
    @ResponseBody
    public User renew(@RequestBody User user) throws UserNotFoundException {
        return userService.renew(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable int id, @PathVariable int friendId) throws UserNotFoundException {
        userService.removeFromFriends(id, friendId);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> HandleUserNotFoundException(final UserNotFoundException e ) {
        return new ResponseEntity<>(
                Map.of("error", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}

