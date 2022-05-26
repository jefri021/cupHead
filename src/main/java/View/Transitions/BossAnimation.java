package View.Transitions;

import Model.Game;
import View.Images;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BossAnimation extends Transition {

    private boolean goingUp;

    public BossAnimation() {
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(600));
        goingUp = true;
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 5);
        Game.getBoss().setImage(Images.valueOf("BOSS_" + frame).getImg());
        if (goingUp && Game.getBoss().getY() < -200)
            goingUp = false;
        else if (!goingUp && Game.getBoss().getY() > 300) goingUp = true;

        if (goingUp) Game.getBoss().setY(Game.getBoss().getY() - 3);
        else Game.getBoss().setY(Game.getBoss().getY() + 3);
    }
}
