package View.Transitions;

import View.Components.Egg;
import javafx.animation.Transition;
import javafx.util.Duration;

public class EggAnimation extends Transition {

    private final Egg egg;

    public EggAnimation (Egg egg) {
        this.egg = egg;
        this.setCycleDuration(Duration.millis(1250));
        this.setCycleCount(4);
    }

    @Override
    protected void interpolate(double frac) {
        int rotation = (int)Math.floor(frac * 12) * 30;
        egg.setRotate(rotation);
        egg.move(15);
    }
}
