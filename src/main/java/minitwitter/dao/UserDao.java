package minitwitter.dao;

import minitwitter.model.Message;
import minitwitter.model.User;

import java.util.List;

public interface UserDao {

    User create(final User user);

    List<User> findAll();

    User findUserById(int id);

    List<Message> fetchMessages(int id);

    List<User> findUserFollowers(int id);
}
