package minitwitter.dao;

import minitwitter.model.User;

import java.util.List;

public interface UserDao {

    public User create(final User user);

    public List<User> findAll();

    public User findUserById(int id);
}
