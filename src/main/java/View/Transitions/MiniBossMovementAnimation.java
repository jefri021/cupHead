package View.Transitions;

import Model.Game;
import View.Components.MiniBoss;
import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MiniBossMovementAnimation extends Transition {
    private final MiniBoss miniBoss;

    public MiniBossMovementAnimation (MiniBoss miniBoss, Pane parent) {
        this.miniBoss = miniBoss;
        this.setCycleDuration(Duration.seconds(11));
        this.setCycleCount(1);
        this.setOnFinished(event -> {
            Game.getAllMiniBosses().remove(miniBoss);
            parent.getChildren().remove(miniBoss);
        });
    }

    @Override
    protected void interpolate(double frac) {
        miniBoss.setX(miniBoss.getX() - 5);
    }
}
