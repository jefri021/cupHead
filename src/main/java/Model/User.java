package Model;

import Application.App;

public class User {
    private String username;
    private String password;
    private int avatarPicNumber;
    private double score;

    public User (String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
        avatarPicNumber = 9;
    }

    public void changeUsername (String newUsername) {
        this.username = newUsername;
        App.writeDataToJson();
    }

    public void changePassword (String newPassword) {
        this.password = newPassword;
        App.writeDataToJson();
    }

    public void changeAvatar (int avatarPicNumber) {
        System.out.println("avatar num = " + avatarPicNumber);
        this.avatarPicNumber = avatarPicNumber;
        App.writeDataToJson();
    }

    public void updateScore (double score) {
        if (score > this.score) this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAvatarPicNumber() {
        return avatarPicNumber;
    }

    public double getScore() {
        return score;
    }
}
