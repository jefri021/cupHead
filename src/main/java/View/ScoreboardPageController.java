package View;

import Application.App;
import Model.Database;
import Model.User;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;

public class ScoreboardPageController {

    public Pane background;
    public VBox list;

    public void initialize() {
        ScrollPane scrollPane = (ScrollPane)background.getChildren().get(1);

        scrollPane.setStyle("-fx-background: rgba(0,0,0,0.16); -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);

        ArrayList <User> sorted = new ArrayList<>(Database.getInstance().getUsers());
        Comparator <User> cmp = Comparator.comparing(User::getScore).reversed().thenComparing(User::getUsername);
        sorted.sort(cmp);

        for (User user : sorted) {
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            hBox.setMaxSize(list.getMaxWidth(), 15);
            Label label = new Label();
            label.setText(user.getUsername() + "\t\t" + user.getScore());
            if (sorted.indexOf(user) == 0) label.setId("1TextScoreboard");
            else if (sorted.indexOf(user) == 1) label.setId("2TextScoreboard");
            else if (sorted.indexOf(user) == 2) label.setId("3TextScoreboard");
            else label.setId("TextScoreboard");
            hBox.getChildren().add(label);
            hBox.setStyle("-fx-background-color: rgba(0,0,0,0);" +
                            "-fx-border-width: 1;" + "-fx-border-color: white");
            list.getChildren().add(hBox);
        }
        list.setStyle("-fx-background-color: rgba(0,0,0,0)");
    }

    public void returnMenu() {
        App.changePage("MainMenuPage");
    }
}
