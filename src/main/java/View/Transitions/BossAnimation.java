package View.Transitions;

import Model.Game;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BossAnimation extends Transition {

    public BossAnimation() {
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(600));
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 5);
        Game.getBoss().setImage(new Image("/Images/Game/Boss/" + frame + ".gif"));
    }
}
