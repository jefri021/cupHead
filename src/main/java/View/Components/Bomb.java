package View.Components;

import Model.Game;
import View.Transitions.BombExplosionAnimation;
import View.Transitions.BombMovementAnimation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bomb extends ImageView {

    public Bomb() {
        this.setImage(new Image("/Images/Game/Bomb/Travel/0.png"));
        this.setX(Game.getPlane().getX() + 20);
        this.setY(Game.getPlane().getY() + 60);
    }

    public void move (double dx, double dy) {
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }

    BombMovementAnimation animation;

    public void fire() {
        animation = new BombMovementAnimation(this);
        animation.play();
    }

    public void explode (Pane parent) {
        if (animation != null) animation.stop();
        BombExplosionAnimation explosionAnimation = new BombExplosionAnimation(this);
        explosionAnimation.play();
        Game.getAllBombs().remove(this);
        explosionAnimation.setOnFinished(event -> parent.getChildren().remove(this));
    }
}
