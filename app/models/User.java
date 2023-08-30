package models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class User {
    public String username;
    public String password;
    public String college;
    public String role;

    public User(String username, String password, String college, String role) {
        this.username = username;
        this.password = password;
        this.college = college;
        this.role = role;
    }

    public User() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCollege() {
        return college;
    }

    public String getRole() {
        return role;
    }

    private static Set<User> users;

    static {
        users = new HashSet<>();
        users.add(new User("user1", "pswd", "IIT", "ADMIN"));
        users.add(new User("user2", "pswd", "IIM", "STUDENT"));
        users.add(new User("user3", "pswd", "GNITS", "TEACHER"));
    }

    public static Set<User> usersList(){
        return users;
    }

    public static Optional<User> findUser(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public static void add(User user){
        users.add(user);
    }

    public static void remove(User user){
        users.remove(user);
    }

    public Boolean validateCreds(User loggedInUser){
        if (this.username.equals(loggedInUser.username) && this.password.equals(loggedInUser.password) && this.college.equals(loggedInUser.college) && this.role.equals(loggedInUser.role)) {
            return true;
        } else return false;
    }

    public boolean isAdmin() {
        return this.role.equals("ADMIN");
    }
}

