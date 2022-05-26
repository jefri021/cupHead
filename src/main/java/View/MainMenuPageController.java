package View;

import Application.App;
import Model.Database;
import Model.Game;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MainMenuPageController implements DefaultAnimation {
    public Pane background;
    private Timeline timeline;
    private MediaPlayer mainTheme;

    public void initialize() {
        Media media = new Media(this.getClass().getResource("/Audios/opening.mp3").toString());
        mainTheme = new MediaPlayer(media);
        mainTheme.play();
        mainTheme.setCycleCount(-1);
        startGameInit();
        initCustomButton((VBox)background.getChildren().get(1));
        initCustomButton((VBox)background.getChildren().get(2));
        timeline = App.getTimeLine(background, this);
        timeline.play();
    }

    private void startGameInit() {
        VBox box = (VBox) background.getChildren().get(0);
        ImageView imageView = (ImageView) box.getChildren().get(0);
        Label label = (Label) box.getChildren().get(1);

        box.setOnMouseEntered(mouseEvent ->  {
                box.setLayoutX(524);
                box.setLayoutY(30);
                box.setMaxSize(imageView.getFitWidth() * 1.2, imageView.getFitHeight() * 1.2);
                imageView.setFitHeight(imageView.getFitHeight() * 1.2);
                imageView.setFitWidth(imageView.getFitWidth() * 1.2);
                imageView.setX(-46);
                imageView.setY(5);
                finalizeIMG(imageView);
                flashLabel(label, true);
        });

        box.setOnMouseExited(mouseEvent ->  {
                box.setLayoutX(570);
                box.setLayoutY(35);
                box.setMaxSize(imageView.getFitWidth() * 0.83333, imageView.getFitHeight() * 0.83333);
                imageView.setFitHeight(imageView.getFitHeight() * 0.83333);
                imageView.setFitWidth(imageView.getFitWidth() * 0.83333);
                imageView.setX(0);
                imageView.setY(0);
                finalizeIMG(imageView);
                flashLabel(label, false);
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

        imageView.setOnMouseEntered(mouseEvent -> imageView.setCursor(Cursor.HAND));

        label.setOnMouseEntered(mouseEvent -> label.setCursor(Cursor.HAND));
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
        mainTheme.stop();
        timeline.stop();
        App.changePage("GamePage");
    }

    public void profile() {
        mainTheme.stop();
        timeline.stop();
        App.changePage("ProfileMenuPage");
    }

    public void scoreBoard() {
        mainTheme.stop();
        timeline.stop();
        App.changePage("ScoreboardPage");
    }

    @Override
    public void setTimeLine(Pane pane, double xPosition) {
        String style = "-fx-background-position: " +
                "left " + xPosition * 0.5 * 3+ "px top," +
                "left " + xPosition * 3 + "px bottom," +
                "left " + xPosition * 1.1 * 3 + "px bottom;";
        pane.setStyle(style);
    }
}
