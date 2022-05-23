package View.Components;

import View.Transitions.PlaneBlinkAnimation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Plane extends ImageView {

    private double health;

    public Plane() {
        this.setImage(new Image("/Images/Game/Plane/0.gif"));
        this.setFitHeight(90);
        this.setFitWidth(110);
        this.health = 50;
    }

    public void moveLeft() {
        if (this.getX() > 2) this.setX(this.getX() - 5);
    }

    public void moveRight() {
        if (this.getX() < 1485) this.setX(this.getX() + 5);
    }

    public void moveDown() {
        if (this.getY() < 701) this.setY(this.getY() + 5);
    }

    public void moveUp() {
        if (this.getY() > 2) this.setY(this.getY() - 5);
    }

    public void hitEgg() {
        this.health -= 10;
        new PlaneBlinkAnimation().play();
    }

    public void hitMiniBoss() {
        this.health -= 5;
        new PlaneBlinkAnimation().play();
    }

    public double getHealth() {
        return health;
    }
}
