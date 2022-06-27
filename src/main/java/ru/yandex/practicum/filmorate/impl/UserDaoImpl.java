package ru.yandex.practicum.filmorate.impl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.utils.UserMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)" +
                        "VALUES (?, ?, ?, ?);",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(jdbcTemplate.query("SELECT ID FROM USERS WHERE EMAIL = ?",
                (rs, rowNum) -> rs.getInt("id"),
                user.getEmail()).stream().findAny().orElse(0));
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        jdbcTemplate.update("UPDATE USERS SET " +
                        "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?;",
                updatedUser.getEmail(),
                updatedUser.getLogin(),
                updatedUser.getName(),
                updatedUser.getBirthday());
        return updatedUser;
    }

    @Override
    public Set<User> getAllUsers() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM USERS",
                new UserMapper()));
    }

    @Override
    public void addToFriends(int firstId, int secondId) {
        jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)", firstId, secondId);
    }

    @Override
    public void removeFromFriends(int firstId, int secondId) {
        jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", firstId, secondId);
    }

    @Override
    public Set<User> getFriends(int id) {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM USERS WHERE ID IN " +
                        "(SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)",
                new UserMapper(), id));
    }

    @Override
    public Optional<User> getUserById(Integer id) {

        return jdbcTemplate.query("select * from USERS where ID = ?",
                new UserMapper(), id).stream().findAny();
    }

    @Override
    public Set<User> getCommonFriends(Integer firstId, Integer secondId) {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM USERS " +
                        "WHERE ID IN " +
                        "(SELECT f1.FRIEND_ID " +
                        "FROM FRIENDS f1 " +
                        "INNER JOIN FRIENDS f2 ON f1.FRIEND_ID=f2.FRIEND_ID " +
                        "WHERE f1.USER_ID = ? AND f2.USER_ID = ?)",
                new UserMapper(), firstId, secondId));
    }

    @Override
    public void deleteUserById(Integer id) {
        jdbcTemplate.update("DELETE FROM USERS WHERE ID=?;", id);
    }

    public Set<Integer> getFriendListById(Integer id) {

        String sql = "select friend_id from friends where user_id = ?";
        return Set.copyOf(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id") , id));
    }

    @Override
    public boolean checkForUserExist(int id) {
        return jdbcTemplate.query("select * from USERS where ID = ?",
                new UserMapper(), id).stream().findAny().isEmpty();
    }

}