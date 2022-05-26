package View.Components;

import View.Transitions.PlaneBlinkAnimation;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Plane extends ImageView {

    private double health;
    private final ProgressBar healthBar;

    public Plane() {
        this.setImage(new Image("/Images/Game/Plane/0.gif"));
        this.setFitHeight(90);
        this.setFitWidth(110);
        this.health = 50;
        healthBar = new ProgressBar();
        healthBar.setLayoutX(0);
        healthBar.setLayoutY(-20);
        healthBar.setProgress(1);
        healthBar.setStyle("-fx-accent: #3e79e1;");
    }

    public void moveLeft() {
        if (this.getX() > 2) {
            this.setX(this.getX() - 7);
            healthBar.setLayoutX(this.getX());
        }
    }

    public void moveRight() {
        if (this.getX() < 1485) {
            this.setX(this.getX() + 7);
            healthBar.setLayoutX(this.getX());
        }
    }

    public void moveDown() {
        if (this.getY() < 701) {
            this.setY(this.getY() + 7);
            healthBar.setLayoutY(this.getY() - 20);
        }
    }

    public void moveUp() {
        if (this.getY() > 2) {
            this.setY(this.getY() - 7);
            healthBar.setLayoutY(this.getY() - 20);
        }
    }

    public void hit (String obj) {
        if (obj.equals("egg")) health -= 10;
        if (obj.equals("miniBoss")) health -= 5;
        if (obj.equals("boss")) health -= 20;
        healthBar.setProgress(health / 50);
        if (health < 20) healthBar.setStyle("-fx-accent: #ce0303");
        else if (health < 30) healthBar.setStyle("-fx-accent: #ff8000;");
        new PlaneBlinkAnimation().play();
    }

    public double getHealth() {
        return health;
    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }
}
