package View.Transitions;

import View.Components.Bullet;
import View.Images;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BulletExplosionAnimation extends Transition {
    private final Bullet bullet;

    public BulletExplosionAnimation (Bullet bullet) {
        this.bullet = bullet;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(300));
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 6);
        bullet.setImage(Images.valueOf("BULLET_DUST_" + frame).getImg());
        bullet.setFitWidth(50);
        bullet.setFitHeight(50);
    }
}
