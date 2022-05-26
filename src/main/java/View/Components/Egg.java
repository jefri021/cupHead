package View.Components;

import Model.Game;
import View.Transitions.EggAnimation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Egg extends ImageView {

    public void move (double dx) {
        this.setX(this.getX() - dx);
    }

    public Egg() {
        this.setImage(new Image("/Images/Game/Boss/Attack/Egg/egg.png"));
        this.setX(Game.getBoss().getX() + 70);
        this.setY(Game.getBoss().getY() + Game.getBoss().getImage().getHeight() / 2 - 20);
    }

    public void fire (Pane parent) {
        EggAnimation animation = new EggAnimation(this);
        animation.play();
        animation.setOnFinished(event -> parent.getChildren().remove(Egg.this));
    }
}