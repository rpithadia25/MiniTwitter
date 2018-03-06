package minitwitter.dao.impl;

import minitwitter.dao.UserDao;
import minitwitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    private final String CREATE_USER = "INSERT INTO people(handle, name) values(:handle, :name)";
    private final String GET_ALL_USERS = "SELECT * FROM people";
    private final String GET_USER_BY_ID = "SELECT * FROM people WHERE id = :id";

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
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        return namedParameterJdbcTemplate.queryForObject(GET_USER_BY_ID, parameters, new UserMapper());
    }
}

class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setHandle(rs.getString("handle"));
        user.setName(rs.getString("name"));
        return user;
    }

}
