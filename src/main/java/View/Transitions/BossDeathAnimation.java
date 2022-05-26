package View.Transitions;

import Model.Game;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BossDeathAnimation extends Transition {

    public BossDeathAnimation() {
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(1200));
        setOnFinished(actionEvent -> Game.getBoss().setOpacity(0));
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 25);
        Game.getBoss().setImage(new Image("/Images/Game/Boss/Death/" + frame + ".png"));
    }
}
