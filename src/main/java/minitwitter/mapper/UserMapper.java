package minitwitter.mapper;

import minitwitter.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setHandle(rs.getString("handle"));
        user.setName(rs.getString("name"));
        return user;
    }

}