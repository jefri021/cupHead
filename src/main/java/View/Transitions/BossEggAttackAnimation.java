package View.Transitions;

import Model.Game;
import View.Components.Egg;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BossEggAttackAnimation extends Transition {

    private final Pane parent;
    boolean hasBarfed;

    public BossEggAttackAnimation (Pane parent) {
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(900));
        this.parent = parent;
        hasBarfed = false;
        setOnFinished(event -> Game.getBoss().getBossAnimation().play());
    }
    @Override
    protected void interpolate(double frac) {
        int frame = (int)Math.floor(frac * 11);
        Game.getBoss().setImage(new Image("/Images/Game/Boss/Attack/Egg/" + frame + ".png"));
        if (frame == 3 && !hasBarfed) {
            hasBarfed = true;
            Egg egg = new Egg();
            parent.getChildren().add(parent.getChildren().indexOf(Game.getBoss()) - 1, egg);
            egg.fire(parent);
        }
    }
}
