package View.Transitions;

import View.Components.MiniBoss;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class MiniBossDeathAnimation extends Transition {

    private final MiniBoss miniBoss;

    public MiniBossDeathAnimation (MiniBoss miniBoss) {
        this.miniBoss = miniBoss;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(300));
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 10);
        String address;
        if (miniBoss.isPurple())
            address = "/Images/Game/Boss/MiniBoss/purple/Death/" + frame + ".png";
        else
            address = "/Images/Game/Boss/MiniBoss/yellow/Death/" + frame + ".png";
        miniBoss.setImage(new Image(address));
        miniBoss.setFitWidth(157);
        miniBoss.setFitHeight(114);
    }
}
