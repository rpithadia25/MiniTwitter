package minitwitter.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    Map<String, String> mapUserWthId = new HashMap();

    // HardCoded credentials for now.
    public UserService() {
        mapUserWthId.put("batman", "batman");
        mapUserWthId.put("superman", "superman");
        mapUserWthId.put("catwoman", "catwoman");
        mapUserWthId.put("daredevil", "daredevil");
    }

    public String getUserNameByID(String userId) {
        return mapUserWthId.get(userId);
    }
}
