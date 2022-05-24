package View.Transitions;

import Model.Game;
import View.Components.Bullet;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class BulletFireAnimation extends Transition {

    private final Bullet bullet;

    public BulletFireAnimation (Bullet bullet) {
        this.bullet = bullet;
        this.setCycleCount(2);
        this.setCycleDuration(Duration.millis(20));
    }

    @Override
    protected void interpolate(double frac) {
        if (bullet == null) return;
        int frame = (int)Math.floor(frac * 3);
        bullet.setImage(new Image("/Images/Game/Bullet/Fire/" + frame + ".png"));

        ColorAdjust brightness = new ColorAdjust();
        brightness.setBrightness(-0.1);
        bullet.setEffect(brightness);

        ObservableList <Node> children = bullet.getParent().getChildrenUnmodifiable();
        double planeX = ((ImageView)(children.get(children.indexOf(Game.getPlane())))).getX(),
                planeY = ((ImageView)(children.get(children.indexOf(Game.getPlane())))).getY();
        bullet.setX(planeX + 80);
        bullet.setY(planeY - 5);
    }
}
