package View.Transitions;

import View.Components.MiniBoss;
import View.Images;
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
        String color;
        if (miniBoss.isPurple())
            color = "PURPLE";
        else
            color = "YELLOW";
        miniBoss.setImage(Images.valueOf(color + "_DEATH_" + frame).getImg());
        miniBoss.setFitWidth(157);
        miniBoss.setFitHeight(114);
    }
}
