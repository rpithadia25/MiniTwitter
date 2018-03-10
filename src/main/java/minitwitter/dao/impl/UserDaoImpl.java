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

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    private final String CREATE_USER            = "INSERT INTO people(handle, name) values(:handle, :name)";
    private final String GET_ALL_USERS          = "SELECT * FROM people";
    private final String GET_USER_BY_ID         = "SELECT * FROM people WHERE id = :id";
    private final String GET_FOLLOWERS          = "SELECT * FROM PEOPLE WHERE id IN (SELECT follower_person_id FROM followers WHERE person_id = :id)";
    private final String GET_FOLLOWING          = "SELECT p.* FROM people p, followers f WHERE p.id = f.person_id AND f.follower_person_id = :id";
    private final String GET_MESSAGES           = "SELECT DISTINCT * from MESSAGES m INNER JOIN followers f ON m.person_id = f.person_id WHERE (m.person_id = :id or (m.person_id = f.person_id AND f.follower_person_id = :id))";
    private final String GET_USERID_FROM_HANDLE = "SELECT id FROM people WHERE handle = :handle";
    private final String GET_FOLLOWER_BY_ID     = "SELECT * from PEOPLE WHERE id = (SELECT f.follower_person_id FROM followers f WHERE f.person_id = :userId and f.follower_person_id = :followerUserId)";
    private final String FOLLOW_USER            = "INSERT INTO followers (person_id, follower_person_id) VALUES (:userId, :followerUserId)";
    private final String UNFOLLOW_USER          = "DELETE FROM followers WHERE person_id = :userId and follower_person_id = :followerUserId";

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
    public List<Message> fetchMessages(int id, String searchParameter) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        if (searchParameter != null && !searchParameter.trim().isEmpty()) {
            return namedParameterJdbcTemplate.query(GET_MESSAGES + "AND m.content LIKE '%" + searchParameter + "%'", parameters, new MessageMapper());
        }

        return namedParameterJdbcTemplate.query(GET_MESSAGES, parameters, new MessageMapper());
    }

    @Override
    public List<User> findUserFollowers(int id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return namedParameterJdbcTemplate.query(GET_FOLLOWERS, parameters, new UserMapper());
    }

    @Override
    public List<User> findUserFollowing(int id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return namedParameterJdbcTemplate.query(GET_FOLLOWING, parameters, new UserMapper());
    }

    @Override
    public void followUser(String follower, String userToFollow) {

        int userId = getUserIdFromHandle(userToFollow);
        int followerUserId = getUserIdFromHandle(follower);

        if (followerUserId != userId) {
            Map<String, Integer> parameters = new HashMap<>();
            parameters.put("userId", userId);
            parameters.put("followerUserId", followerUserId);

            System.out.println("Getting Follower list");
            List<User> followers = this.namedParameterJdbcTemplate.query(GET_FOLLOWER_BY_ID, parameters, new UserMapper());

            if (followers == null || followers.size() == 0) {
                this.namedParameterJdbcTemplate.update(FOLLOW_USER, parameters);
            }
        }

    }

    @Override
    public void unfollowUser(String follower, String userToFollow) {

        int userId = getUserIdFromHandle(userToFollow);
        int followerUserId = getUserIdFromHandle(follower);

        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("followerUserId", followerUserId);
        namedParameterJdbcTemplate.update(UNFOLLOW_USER, parameters);
    }

    // Fetches the Username from Http Basic Auth
    public String getHandle(HttpServletRequest request) {
        byte[] decodedBytes = Base64.getDecoder().decode(request.getHeader("Authorization").split(" ")[1]);
        String authorizationContents = new String(decodedBytes);
        return authorizationContents.split(":")[0];
    }

    public int getUserIdFromHandle(String handle) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("handle", handle);
        return namedParameterJdbcTemplate.queryForObject(GET_USERID_FROM_HANDLE, parameters, Integer.class);
    }
}
