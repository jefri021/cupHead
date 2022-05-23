package View.Transitions;

import Model.Game;
import javafx.animation.Transition;
import javafx.util.Duration;

public class PlaneBlinkAnimation extends Transition {

    public PlaneBlinkAnimation() {
        this.setCycleCount(10);
        this.setCycleDuration(Duration.millis(300));
        setOnFinished(event -> {
            if (Game.getPlane().getOpacity() == 0.3) Game.getPlane().setOpacity(1);
        });
    }

    @Override
    protected void interpolate(double frac) {
        if (frac > 0.5) Game.getPlane().setOpacity(0.3);
        else Game.getPlane().setOpacity(1);
    }
}
