package Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database instance;

    private ArrayList <User> users;
    private User loggedInUser;

    private Database() {
        users = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setLoggedInUser (User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean jsonReadSuccessfully() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("UsersList.json")));
            users = new Gson().fromJson(json, new TypeToken<List<User>>() {
            }.getType());
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void createUser (String username, String password) {
        if (this.users == null) this.users = new ArrayList<>();
        this.users.add(new User(username, password));
    }
}
