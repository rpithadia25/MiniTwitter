package minitwitter.controller;

import minitwitter.dao.impl.UserDaoImpl;
import minitwitter.model.Message;
import minitwitter.model.PopularFollower;
import minitwitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/twitter")
public class MiniTwitterController {

    @Autowired
    private UserDaoImpl userDao;

    @GetMapping(value = "/messages/{userId}")
    public List<Message> getMessages(@PathVariable(value = "userId") int userId, @RequestParam(value = "search", required = false) String searchParameter) {
        return userDao.fetchMessages(userId, searchParameter);
    }

    @GetMapping(value = "/followers/{userId}")
    public List<User> getFollowers(@PathVariable(value = "userId") int userId) {
        return userDao.findUserFollowers(userId);
    }

    @GetMapping(value = "/following/{userId}")
    public List<User> getFollowing(@PathVariable(value = "userId") int userId) {
        return userDao.findUserFollowing(userId);
    }

    @PostMapping(value = "/follow/{handle}")
    public void followUser(HttpServletRequest request, @PathVariable(value = "handle") String handle) {
        String userName = userDao.getHandle(request);
        userDao.followUser(userName, handle);
    }

    @DeleteMapping(value = "/unfollow/{handle}")
    public void unfollowUser(HttpServletRequest request, @PathVariable(value = "handle") String handle) {
        String userName = userDao.getHandle(request);
        userDao.unfollowUser(userName, handle);
    }

    @GetMapping(value = "/popularusers")
    public List<PopularFollower> getPopularUsers() {
        return userDao.findPopularUsers();
    }
}
