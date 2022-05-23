package View.Transitions;

import View.Components.Bullet;
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
        String address = "/Images/Game/Bullet/Dust/" + frame + ".png";
        bullet.setImage(new Image(address));
        bullet.setFitWidth(50);
        bullet.setFitHeight(50);
    }
}
