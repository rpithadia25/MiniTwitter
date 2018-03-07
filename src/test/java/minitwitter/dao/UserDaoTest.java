package minitwitter.dao;

import minitwitter.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void createUserTest() {
        User savedUser = userDao.create(getUser());
        User dbUser = userDao.create(savedUser);
        assertEquals(savedUser.getName(), dbUser.getName());
        assertEquals(savedUser.getHandle(), dbUser.getHandle());
    }

    @Test
    public void findAllUsers() {
        List<User> users = userDao.findAll();
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    public void findUserById() {
        User user = userDao.findUserById(1);
        assertNotNull(user);
    }

    @Test
    public void findUserFollowers() {
        List<User> users = userDao.findUserFollowers(1);
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    private User getUser() {
        User user = new User();
        user.setHandle("flash");
        user.setName("Barry Allen");
        return user;
    }
}
