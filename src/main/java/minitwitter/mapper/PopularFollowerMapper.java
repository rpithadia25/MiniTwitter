package minitwitter.mapper;

import minitwitter.model.PopularFollower;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PopularFollowerMapper implements RowMapper<PopularFollower> {

    @Override
    public PopularFollower mapRow(ResultSet rs, int rowNum) throws SQLException {
        PopularFollower popularFollower = new PopularFollower();
        popularFollower.setUserId(rs.getInt("id"));
        popularFollower.setPopularFollowerId(rs.getInt("follower_id"));
        return popularFollower;
    }
}
