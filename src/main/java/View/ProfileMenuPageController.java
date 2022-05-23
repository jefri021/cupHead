package View;

import Application.App;
import Controller.ProfileController;
import Model.Database;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ProfileMenuPageController implements DefaultAnimation {
    public Pane background;

    public TextField usernameField;
    public Label usernameLabel;

    public TextField passwordField;
    public Label passwordLabel;

    public ImageView avatarIMG;
    private int avatarPicNumber;
    public Label avatarLabel;

    public void initialize() {
        usernameField = (TextField)((VBox)background.getChildren().get(0)).getChildren().get(1);
        usernameLabel = (Label)((VBox)background.getChildren().get(0)).getChildren().get(2);

        passwordField = (TextField)((VBox)background.getChildren().get(0)).getChildren().get(6);
        passwordLabel = (Label)((VBox)background.getChildren().get(0)).getChildren().get(7);

        usernameField.setCursor(Cursor.TEXT);
        passwordField.setCursor(Cursor.TEXT);

        App.getTimeLine(background, this).play();
    }

    public void changeUsername() {
        if (usernameField.getText().length() > 0)
            showMessage(usernameLabel, ProfileController.changeUsername(usernameField));
    }

    public void changePassword() {
        if (passwordField.getText().length() > 0)
            showMessage(passwordLabel, ProfileController.changePassword(passwordField));
    }

    public void nextAvatar() {
        avatarPicNumber++;
        avatarPicNumber %= 10;
        avatarIMG.setImage(new Image("/Images/Avatars/" + avatarPicNumber + ".png"));
        Rectangle clip = new Rectangle(avatarIMG.getFitWidth(), avatarIMG.getFitHeight());
        clip.setX(avatarIMG.getX());
        clip.setY(avatarIMG.getY());
        clip.setArcHeight(30);
        clip.setArcWidth(30);
        avatarIMG.setClip(clip);
    }

    public void changeAvatar() {
        showMessage(avatarLabel, ProfileController.changeAvatar(avatarPicNumber));
    }

    private void showMessage (Label label, String message) {
        label.setText(message);
        if (message.endsWith("successfully")) label.setTextFill(Color.color(0, 0.68, 0.2));
        else label.setTextFill(Color.color(0.82, 0, 0));
    }

    public void logout() {
        Database.getInstance().setLoggedInUser(null);
        App.changePage("LoginAndRegistrationPage");
    }

    public void delete() {
        Database database = Database.getInstance();
        database.getUsers().remove(database.getLoggedInUser());
        App.writeDataToJson();
        App.changePage("LoginAndRegistrationPage");
    }

    public void backToMainMenu() {
        App.changePage("MainMenuPage");
    }

    public void eraseTexts() {
        usernameField.setText("");
        usernameLabel.setText("");

        passwordField.setText("");
        passwordLabel.setText("");

        avatarLabel.setText("");
    }

    @Override
    public void setTimeLine(Pane pane, double xPosition) {
        String style = "-fx-background-position: " +
                "left " + xPosition * 2 + "px top," +
                "left " + xPosition * 1.2 * 2 + "px bottom," +
                "left " + xPosition * 1.4 * 2 + "px bottom," +
                "left " + xPosition * 1.6 * 2 + "px bottom;";
        pane.setStyle(style);
    }
}
