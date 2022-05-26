package View.Transitions;

import Model.Game;
import View.Images;
import javafx.animation.Transition;
import javafx.util.Duration;

public class BossAnimation extends Transition {

    private boolean goingUp = false;

    public BossAnimation() {
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(600));
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 5);
        Game.getInstance().getBoss().setImage(Images.valueOf("BOSS_" + frame).getImg());

        if (goingUp) {
            if (Game.getInstance().getBoss().getY() > -100)
                Game.getInstance().getBoss().setY(Game.getInstance().getBoss().getY() - 2);
            else
                goingUp = false;
        }
        else {
            if (Game.getInstance().getBoss().getY() < 500)
                Game.getInstance().getBoss().setY(Game.getInstance().getBoss().getY() + 2);
            else
                goingUp = true;
        }
    }
}
