package View.Transitions;

import View.Components.Bomb;
import View.Images;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BombExplosionAnimation extends Transition {

    private final Bomb bomb;

    public BombExplosionAnimation (Bomb bomb) {
        this.bomb = bomb;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(700));
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 6);
        bomb.setImage(Images.valueOf("BOMB_DUST_" + frame).getImg());
    }
}
