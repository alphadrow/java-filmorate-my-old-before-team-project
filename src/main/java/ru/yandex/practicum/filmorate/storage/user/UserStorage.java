package ru.yandex.practicum.filmorate.storage.user;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    public Set<User> findAll();
    public User create(User user);
    public User renew(User user) throws UserNotFoundException;
    public Optional<User> getById(long id);
}
