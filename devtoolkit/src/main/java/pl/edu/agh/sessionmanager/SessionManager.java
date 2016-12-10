package pl.edu.agh.sessionmanager;

import org.json.JSONObject;
import pl.edu.agh.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamil on 22.11.2016.
 */
public class SessionManager {
    public static Map<String, User> sessionIds  = new HashMap<>();

    public static void addId(String json) {
        JSONObject jsonObj = new JSONObject(json);
        SessionManager.sessionIds.put(jsonObj.getString("sessionId"), new User().login(jsonObj.getString("username")).role(jsonObj.getString("role")));
    }

    public static void flush() {
        sessionIds.clear();
    }

    public static void deleteId(String json) {
        JSONObject jsonObj = new JSONObject(json);
        sessionIds.remove(jsonObj.getString("sessionId"));
    }
}
