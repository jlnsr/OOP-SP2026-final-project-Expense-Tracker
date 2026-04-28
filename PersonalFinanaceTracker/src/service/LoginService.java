package service;

import model.User;
import java.util.List;

public class LoginService {

    private List<User> users;

    public LoginService(List<User> users) {
        this.users = users;
    }

    // Returns the matched User if credentials are valid, null otherwise
    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}