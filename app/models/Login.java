package models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Login {
    public String username;
    public String password;
    public String college;

    public Login(String username, String password, String college) {
        this.username = username;
        this.password = password;
        this.college = college;
    }

    public Login() {
    }

    private static Set<Login> loggedInUsers;

    public static void setLoggedInUsers(Set<Login> loggedInUsers) {
        Login.loggedInUsers = loggedInUsers;
    }

    public static Set<Login> getLoggedInUsers() {
        return loggedInUsers;
    }

    public static Optional<Login> findUser(String username) {
        for (Login loggedInUser : getLoggedInUsers()){
            if (loggedInUser.username.equals(username)) {
                return Optional.of(loggedInUser);
            }
        }
        return Optional.empty();
    }

    static {
        loggedInUsers = new HashSet<>();
    }

    public static void addUser(Login loggedInUser) {
        System.out.println("Added user " + loggedInUser.username);
        loggedInUsers.add(loggedInUser);
    }

    public static void removeUser(Login loggedInUser) {
        System.out.println("removed user " + loggedInUser.username);
        loggedInUsers.remove(loggedInUser);
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCollege() {
        return college;
    }
}
