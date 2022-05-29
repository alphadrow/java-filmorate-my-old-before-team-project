package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
@Component
public class InMemoryUserStorage implements UserStorage{
    private final Set<User> users = new HashSet<>();

    @Override
    public Set<User> findAll() {
        return users;
    }

    @Override
    public User create(User user) {
        users.add(user);
        return user;
    }

    @Override
    public User renew(User user) throws UserNotFoundException {
        removeById(user.getId());
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> getById(long id) {
        return users.stream().filter(user -> user.getId() == id).findAny();
    }

    protected void removeById(long id) throws UserNotFoundException {
        try {
            users.remove(users.stream().filter(user -> user.getId() == id).findAny().get());
        } catch (NoSuchElementException e) {
            throw  new UserNotFoundException("Пользователя с id " + id + "нет в базе.");
        }
    }
}
