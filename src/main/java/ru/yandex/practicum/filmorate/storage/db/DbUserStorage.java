package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class DbUserStorage extends DbBaseStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT u.*, " +
            "(SELECT Listagg(uf.friend_id, ',') FROM user_friend uf WHERE uf.user_id = u.user_id) as friends " +
            "FROM users u";
    private static final String FIND_BY_ID_QUERY = "SELECT u.*, " +
            "(SELECT Listagg(uf.friend_id, ',') FROM user_friend uf WHERE uf.user_id = u.user_id) as friends " +
            "FROM users u " +
            "where u.user_id = ?";

    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birth_date)" +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birth_date = ? " +
            "WHERE user_id = ?";

    private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?";

    private static final String ADD_FRIEND_QUERY = "MERGE INTO user_friend KEY(user_id, friend_id) values(?, ?)";

    private static final String DELETE_FRIEND_QUERY = "DELETE FROM user_friend WHERE user_id = ? AND friend_id =?";

    public DbUserStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    @Override
    public Collection<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User getById(Long id) {
        Optional<User> user = findOne(FIND_BY_ID_QUERY, id);
        return user.orElse(null);
    }

    @Override
    public User add(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday())
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        return user;
    }

    @Override
    public void delete(Long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public void addFriend(User user, User friend) {
        update(
                ADD_FRIEND_QUERY,
                user.getId(),
                friend.getId()
        );
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        delete(
                DELETE_FRIEND_QUERY,
                userId,
                friendId
        );
    }
}
