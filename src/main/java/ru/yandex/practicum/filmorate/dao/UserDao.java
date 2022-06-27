package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserDao {

    User createUser(User user);

    User updateUser(User updatedUser);

    Set<User> getAllUsers();

    void addToFriends(int firstId, int secondId);

    void removeFromFriends(int firstId, int secondId);
    Set<User> getFriends(int id);
    Optional<User> getUserById(Integer id);

    Set<User> getCommonFriends(Integer firstId, Integer secondId);

    void deleteUserById(Integer id);

    boolean checkForUserExist(int id);
}
