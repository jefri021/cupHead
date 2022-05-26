package View.Transitions;

import View.Components.Bullet;
import View.Images;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BulletMovementAnimation extends Transition {
    private final Bullet bullet;

    public BulletMovementAnimation(Bullet bullet) {
        this.bullet = bullet;
        this.setCycleDuration(Duration.millis(400));
        this.setCycleCount(5);
    }

    @Override
    protected void interpolate (double frac) {
        int frame = (int)Math.floor(frac * 8);
        bullet.setImage(Images.valueOf("BULLET_" + frame).getImg());
        bullet.move(20);
    }
}
