package ru.yandex.practicum.filmorate.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao ;
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

    public void addToFriends(int firstId, int otherId) throws UserNotFoundException {
        checkForExist(firstId);
        checkForExist(otherId);
        userDao.addToFriends(firstId, otherId);
    }

    public void removeFromFriends(int firstId, int otherId) {
        userDao.removeFromFriends(firstId, otherId);
    }

    public Set<User> getFriends(int id) {
        return userDao.getFriends(id);
    }

    public Set<User> getCommonFriends(int firstId, int otherId) {
        return userDao.getCommonFriends(firstId, otherId);
    }

    public Set<User> findAll() {
        return userDao.getAllUsers();
    }

    public User getUserById(int id) throws UserNotFoundException {
        return userDao.getUserById(id).orElseThrow(() ->
                new UserNotFoundException("Пользователя с ID " + id + " нет в базе."));
    }

    public User renew(User user) throws UserNotFoundException {
            checkForExist(user.getId());
            return userDao.updateUser(user);

    }

    public User create(User user) {
        log.debug("Получен запрос на добавление пользователя: {}",
                user);
        if (validate(user)) {
            return userDao.createUser(user);
        } else {
            log.warn("Валидация пользователя не пройдена!");
            throw new ValidationException("Неверные параметры пользователя!");
        }
    }

    private void checkForExist(int id) throws UserNotFoundException {
        if (userDao.getUserById(id).isEmpty()) {
            throw new UserNotFoundException("Пользователя с ID " + id + " нет в базе.");
        }
    }

}
