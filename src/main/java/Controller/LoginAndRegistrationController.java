package Controller;

import Model.Database;
import Model.User;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class LoginAndRegistrationController {

    public static void readJsonFile() {
        if (!Database.getInstance().jsonReadSuccessfully()) System.out.println("couldnt read");
    }

    public static String output (String username, String password) {
        Database database = Database.getInstance();
        try {
            for (User user : database.getUsers())
                if (user.getUsername().equals(username))
                    return "username already exists";
        }
        catch (Exception e) {
            System.out.println("Initializing JSON file...");
        }

        if (!isUsernameValid(username))
            return "username format is invalid";
        if (!isPasswordStrong(password))
            return "password is weak";
        database.createUser(username, password);
        return "user registered successfully";
    }

    public static boolean isUsernameValid (String username) {
        String smallCase = username.toLowerCase();
        if (!smallCase.matches("^.*[a-z].*$")) return false;
        return smallCase.matches("^[a-z0-9]+$") && username.length() < 10;
    }

    public static boolean isPasswordStrong (String password) {
        return password.length() >= 8 && password.matches(".*[0-9].*") && password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*");
    }

    public static boolean loggedInSuccessfully (String username, String password) {
        Database database = Database.getInstance();
        System.out.println("database = " + (database != null));
        System.out.println("users = " + (database.getUsers() != null));
        for (User user : database.getUsers())
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                database.setLoggedInUser(user);
                return true;
            }
        return false;
    }
}