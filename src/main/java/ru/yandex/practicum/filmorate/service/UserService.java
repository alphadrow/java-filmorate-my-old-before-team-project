package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j

@Service
public class UserService {

    UserStorage userStorage;
    long id;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public boolean validate(User user){
        if (user.getEmail() == null){
            log.debug("user.getEmail(): {}", user.getEmail());
            return false;
        }
        if (!user.getEmail().contains("@")) {
            log.debug("!user.getEmail().contains(\"@\"): {}", !user.getEmail().contains("@"));
            return false;
        }
        if (user.getEmail().isBlank()) {
            log.debug("user.getEmail().isBlank(): {}", user.getEmail().isBlank());
            return false;
        }
        if (user.getLogin() == null) {
            log.debug("user.getLogin() == null");
            return false;
        }
        if (user.getLogin().isBlank()) {
            log.debug("user.getLogin().isBlank(): {}", user.getLogin().isBlank());
            return false;
        }
        if (user.getLogin().contains(" ")) {
            log.debug("user.getLogin().contains(\" \"): {}", user.getLogin().contains(" "));
            return false;
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("user birthday is after today");
            return false;
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.debug("setName trigger");
            user.setName(user.getLogin());
        }
        return true;
    }

    public void addToFriends(long firstId, long otherId) throws UserNotFoundException {
        User user1 = getUserById(firstId);
        User user2 = getUserById(otherId);
        user1.addToFriends(otherId);
        user2.addToFriends(firstId);
    }

    public void removeFromFriends(long firstId, long otherId) throws UserNotFoundException {
        User user1 = getUserById(firstId);
        User user2 = getUserById(otherId);
        user1.removeFromFriends(otherId);
        user2.removeFromFriends(firstId);
    }

    public Set<User> getFriends(long id) throws UserNotFoundException {
        Set<User> result = new HashSet<>();
        for (Long friendId : getUserById(id).getFriends()) {
            result.add(getUserById(friendId));
        }
        return result;
    }

    public Set<User> getCommonFriends(long firstId, long otherId) throws UserNotFoundException {
        User user1 = getUserById(firstId);
        User user2 = getUserById(otherId);
        Set<User> result = new HashSet<>();
        try {
            for (long id1 : user1.getFriends()) {
                for (long id2 : user2.getFriends()) {
                    if (id1 == id2) {
                        result.add(getUserById(id1));
                    }
                }
            }
        } catch (NullPointerException e) {
                return result;
        }
        return result;
    }

    public Set<User> findAll() {
        return userStorage.findAll();
    }

    public User getUserById(long id) throws UserNotFoundException {
        if (userStorage.getById(id).isPresent()){
            return userStorage.getById(id).get();
        } else {
            throw new UserNotFoundException("Пользователя с ID " + id + " нет в базе.");
        }
    }

    public User renew(User user) throws UserNotFoundException {
        return userStorage.renew(user);
    }

    public User create(User user) {
        log.debug("Получен запрос на добавление пользователя: {}",
                user);
        if (validate(user)) {
            log.debug("user: {}", user);
            user.setId(++id);
            userStorage.create(user);
            return user;
        } else {
            log.warn("Валидация пользователя не пройдена!");
            throw new ValidationException("Неверные параметры пользователя!");
        }
    }

}
