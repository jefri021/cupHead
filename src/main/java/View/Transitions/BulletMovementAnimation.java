package View.Transitions;

import View.Components.Bullet;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BulletMovementAnimation extends Transition {
    private final Bullet bullet;

    public BulletMovementAnimation(Bullet bullet, Pane parent) {
        this.bullet = bullet;
        this.setCycleDuration(Duration.millis(400));
        this.setCycleCount(5);
        this.setOnFinished(event -> parent.getChildren().remove(bullet));
    }

    @Override
    protected void interpolate (double frac) {
        int frame = (int)Math.floor(frac * 8);
        bullet.setImage(new Image("/Images/Game/Bullet/Travel/" + frame + ".png"));
        bullet.move(20);
    }
}
