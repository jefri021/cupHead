package View.Transitions;

import View.Components.Bomb;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BombMovementAnimation extends Transition {

    private final Bomb bomb;

    public BombMovementAnimation (Bomb bomb) {
        this.bomb = bomb;
        this.setCycleCount(10);
        this.setCycleDuration(Duration.millis(150));
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 6);
        bomb.setImage(new Image("/Images/Game/Bomb/Travel/" + frame + ".png"));
        bomb.move(8, 10 + (int)Math.floor(frac * 5));
    }
}
