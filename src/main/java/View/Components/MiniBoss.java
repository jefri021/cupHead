package View.Components;

import Model.Game;
import View.Transitions.MiniBossDeathAnimation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class MiniBoss extends ImageView {
    private final boolean isPurple;
    private int HP;

    public boolean isPurple() {
        return isPurple;
    }

    public MiniBoss(int startingX, int startingY, boolean isPurple) {
        this.setX(startingX);
        this.setY(startingY);
        if (isPurple) this.setImage(new Image("/Images/Game/Boss/MiniBoss/purple/purple.gif"));
        else this.setImage(new Image("/Images/Game/Boss/MiniBoss/yellow/yellow.gif"));
        this.isPurple = isPurple;
        this.HP = 3;
    }

    public void hit (Pane parent, boolean isTerminal) {
        if (isTerminal) HP = 0;
        else HP--;
        if (HP > 0) return;
        Game.getAllMiniBosses().remove(MiniBoss.this);
        MiniBossDeathAnimation animation = new MiniBossDeathAnimation(this);
        animation.play();
        animation.setOnFinished(event -> parent.getChildren().remove(MiniBoss.this));
    }
}
