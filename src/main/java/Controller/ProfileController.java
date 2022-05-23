package Controller;

import Model.Database;
import Model.User;
import javafx.scene.control.TextField;

public class ProfileController {
    public static String changeUsername (TextField field) {
        Database database = Database.getInstance();

        for (User user : database.getUsers())
            if (user.getUsername().equals(field.getText()))
                return "username is already taken";

        if (!LoginAndRegistrationController.isUsernameValid(field.getText()))
            return "username format is invalid";

        database.getLoggedInUser().changeUsername(field.getText());
        return "username changed successfully";
    }

    public static String changePassword (TextField field) {
        if (LoginAndRegistrationController.isPasswordStrong(field.getText())) {
            Database.getInstance().getLoggedInUser().changePassword(field.getText());
            return "password changed successfully";
        }
        else
            return "new password is weak";
    }

    public static String changeAvatar (int avatarPicNumber) {
        Database database = Database.getInstance();
        for (User user : database.getUsers()) {
            if (user.equals(database.getLoggedInUser())) continue;
            if (user.getAvatarPicNumber() == avatarPicNumber && avatarPicNumber != 9) return "this avatar is already taken";
        }
        database.getLoggedInUser().changeAvatar(avatarPicNumber);
        return "avatar changed successfully";
    }
}
