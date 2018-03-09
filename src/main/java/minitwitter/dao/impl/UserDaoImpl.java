package minitwitter.dao.impl;

import minitwitter.dao.UserDao;
import minitwitter.mapper.MessageMapper;
import minitwitter.mapper.UserMapper;
import minitwitter.model.Message;
import minitwitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    private final String CREATE_USER = "INSERT INTO people(handle, name) values(:handle, :name)";
    private final String GET_ALL_USERS = "SELECT * FROM people";
    private final String GET_USER_BY_ID = "SELECT * FROM people WHERE id = :id";
    private final String GET_FOLLOWERS = "SELECT * FROM PEOPLE WHERE id IN (SELECT follower_person_id FROM followers WHERE person_id = :id)";
    private final String GET_MESSAGES = "SELECT DISTINCT * from MESSAGES m INNER JOIN followers f ON m.person_id = f.person_id WHERE (m.person_id = :id or (m.person_id = f.person_id AND f.follower_person_id = :id))";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User create(final User user) {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("handle", user.getHandle())
                .addValue("name", user.getName());
        namedParameterJdbcTemplate.update(CREATE_USER, parameters, holder);
        user.setId(holder.getKey().intValue());
        return user;
    }

    @Override
    public List<User> findAll() {
        return namedParameterJdbcTemplate.query(GET_ALL_USERS, new UserMapper());
    }

    @Override
    public User findUserById(int id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return namedParameterJdbcTemplate.queryForObject(GET_USER_BY_ID, parameters, new UserMapper());
    }

    @Override
    public List<Message> fetchMessages(int id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return namedParameterJdbcTemplate.query(GET_MESSAGES, parameters, new MessageMapper());
    }

    @Override
    public List<User> findUserFollowers(int id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return namedParameterJdbcTemplate.query(GET_FOLLOWERS, parameters, new UserMapper());
    }
}
