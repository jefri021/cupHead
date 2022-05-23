package Application;

import Model.Database;
import View.DefaultAnimation;
import com.google.gson.Gson;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class App extends Application {

    public static Scene scene;

    public static void main(String[] args) {
        launch(args);
        writeDataToJson();
    }

    public static void writeDataToJson() {
        try {
            FileWriter fileWriter = new FileWriter("UsersList.json");
            fileWriter.write(new Gson().toJson(Database.getInstance().getUsers()));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = (Pane) loadFXML("LoginAndRegistrationPage");
        Scene scene = new Scene(pane);
        App.scene = scene;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("CupHead");
        stage.getIcons().add(new Image("Images/icon.png"));
        stage.show();
    }

    public static void changePage (String fileName) {
        Parent root = loadFXML(fileName);
        App.scene.setRoot(root);
    }

    private static Parent loadFXML(String fileName) {
        try {
            URL address = new URL(App.class.getResource("/FXML/" + fileName + ".fxml").toExternalForm());
            return FXMLLoader.load(address);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static Timeline getTimeLine(Pane background, DefaultAnimation defaultAnimation) {
        DoubleProperty xPosition = new SimpleDoubleProperty(0);
        xPosition.addListener((observable, oldValue, newValue) -> defaultAnimation.setTimeLine(background, xPosition.get()));

        return new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(xPosition, 0)),
                new KeyFrame(Duration.minutes(10), new KeyValue(xPosition, -15000))
        );
    }
}
