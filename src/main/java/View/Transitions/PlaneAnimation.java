package View.Transitions;

import Model.Game;
import View.Images;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class PlaneAnimation extends Transition {

    private final String direction;
    private final boolean zeroToTen;

    public PlaneAnimation(String direction, boolean zeroToTen) {
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(200));
        this.zeroToTen = zeroToTen;
        this.direction = direction;
        if (zeroToTen)
            setOnFinished(event -> Game.getPlane().setImage(Images.valueOf("PLANE_" + direction.toUpperCase() + "_IDLE").getImg()));
        else
            setOnFinished(event -> Game.getPlane().setImage(Images.PLANE_IDLE.getImg()));
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 10);
        if (!zeroToTen) frame = 10 - frame;
        Game.getPlane().setImage(Images.valueOf("PLANE_" + direction.toUpperCase() + "_" + frame).getImg());
    }
}
