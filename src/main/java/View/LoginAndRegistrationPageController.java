package View;

import Application.App;
import Controller.LoginAndRegistrationController;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginAndRegistrationPageController implements DefaultAnimation {

    public Pane background;
    public TextField username;
    public TextField password;
    private Timeline timeLine;

    public void login() {
        if (username.getText().length() == 0 || password.getText().length() == 0) return;
        if (LoginAndRegistrationController.loggedInSuccessfully(username.getText(), password.getText())) {
            timeLine.stop();
            App.changePage("MainMenuPage");
        }
        else showMessage("incorrect username or password");
    }

    public void register() {
        if (username.getText().length() == 0 || password.getText().length() == 0) return;
        String output = LoginAndRegistrationController.output(username.getText(), password.getText());
        showMessage(output);
    }

    public void playAsGuest() {

    }

    public void initialize() {
        LoginAndRegistrationController.readJsonFile();

        username.setCursor(Cursor.TEXT);
        password.setCursor(Cursor.TEXT);

        timeLine = App.getTimeLine(background, this);
        timeLine.play();
    }

    public void showMessage (String message) {
        Label label = (Label) ((VBox)background.getChildren().get(0)).getChildren().get(7);
        label.setText(message);
        if (message.equals("user registered successfully")) label.setTextFill(Color.color(0, 0.68, 0.2));
        else label.setTextFill(Color.color(0.82, 0, 0));
    }

    public void eraseMessage() {
        Label label = (Label) ((VBox)background.getChildren().get(0)).getChildren().get(7);
        label.setText("");
    }

    @Override
    public void setTimeLine (Pane pane, double xPosition) {
        String style = "-fx-background-position: " +
                "left " + xPosition * 0.6 * 3 + "px top," +
                "left " + xPosition * 0.9 * 3 + "px bottom;";
        pane.setStyle(style);
    }
}

