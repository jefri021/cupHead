package View.Transitions;

import Model.Game;
import View.Audios;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BossDeathAnimation extends Transition {

    public BossDeathAnimation() {
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(1200));
        setOnFinished(actionEvent -> {
            Game.getInstance().getBoss().setOpacity(0);
            Audios.BOSS_DEATH.getAudioClip().play();
        });
    }

    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 25);
        Game.getInstance().getBoss().setImage(new Image("/Images/Game/Boss/Death/" + frame + ".png"));
    }
}
