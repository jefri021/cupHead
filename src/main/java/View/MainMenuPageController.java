package View;

import Application.App;
import Model.Database;
import Model.Game;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MainMenuPageController implements DefaultAnimation {
    public Pane background;

    public void initialize() {
        startGameInit();
        initCustomButton((VBox)background.getChildren().get(1));
        initCustomButton((VBox)background.getChildren().get(2));
        App.getTimeLine(background, this).play();
    }

    private void startGameInit() {
        VBox box = (VBox) background.getChildren().get(0);
        ImageView imageView = (ImageView) box.getChildren().get(0);
        Label label = (Label) box.getChildren().get(1);

        box.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                box.setLayoutX(524);
                box.setLayoutY(30);
                box.setMaxSize(imageView.getFitWidth() * 1.2, imageView.getFitHeight() * 1.2);
                imageView.setFitHeight(imageView.getFitHeight() * 1.2);
                imageView.setFitWidth(imageView.getFitWidth() * 1.2);
                imageView.setX(-46);
                imageView.setY(5);
                finalizeIMG(imageView);
                flashLabel(label, true);
            }
        });

        box.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                box.setLayoutX(570);
                box.setLayoutY(35);
                box.setMaxSize(imageView.getFitWidth() * 0.83333, imageView.getFitHeight() * 0.83333);
                imageView.setFitHeight(imageView.getFitHeight() * 0.83333);
                imageView.setFitWidth(imageView.getFitWidth() * 0.83333);
                imageView.setX(0);
                imageView.setY(0);
                finalizeIMG(imageView);
                flashLabel(label, false);
            }
        });
    }

    private void finalizeIMG (ImageView startGameIMG) {
        Rectangle clip = new Rectangle();
        clip.setX(startGameIMG.getX());
        clip.setY(startGameIMG.getY());
        clip.setWidth(startGameIMG.getFitWidth());
        clip.setHeight(startGameIMG.getFitHeight());
        clip.setArcHeight(20);
        clip.setArcWidth(20);
        startGameIMG.setClip(clip);
        startGameIMG.setCursor(Cursor.HAND);
    }

    private void initCustomButton(VBox box) {
        ImageView imageView = (ImageView)box.getChildren().get(0);
        Label label = (Label)box.getChildren().get(1);

        imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.setCursor(Cursor.HAND);
            }
        });

        label.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                label.setCursor(Cursor.HAND);
            }
        });
    }

    private void flashLabel (Label label, boolean doFlash) {
        double flashPeriod;
        if (doFlash) flashPeriod = 0.3;
        else flashPeriod = 10000;
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(flashPeriod), label);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();
    }

    public void startGame() {
        Game.startNewGame(Database.getInstance().getLoggedInUser());
        App.changePage("GamePage");
    }

    public void profile() {
        App.changePage("ProfileMenuPage");
    }

    public void scoreBoard() {
        App.changePage("ScoreboardPage");
    }

    @Override
    public void setTimeLine(Pane pane, double xPosition) {
        String style = "-fx-background-position: " +
                "left " + xPosition * 0.5 * 3+ "px top," +
                "left " + xPosition * 3 + "px top," +
                "left " + xPosition * 1.1 * 3 + "px bottom;";
        pane.setStyle(style);
    }
}
