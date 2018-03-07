package minitwitter.controller;

import minitwitter.dao.impl.UserDaoImpl;
import minitwitter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/twitter")
public class MiniTwitterController {

    @Autowired
    private UserDaoImpl userDao;

    @GetMapping(value = "/followers/{userId}")
    public List<User> getFollowers(@PathVariable(value = "userId") Integer userId) {
        return userDao.findUserFollowers(userId);
    }
}
