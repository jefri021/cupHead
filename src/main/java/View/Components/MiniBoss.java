package View.Components;

import Model.Game;
import View.Images;
import View.Audios;
import View.Transitions.MiniBossDeathAnimation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Random;


public class MiniBoss extends ImageView {
    private final boolean isPurple;
    private int HP;

    public boolean isPurple() {
        return isPurple;
    }

    public MiniBoss(int startingX, int startingY, boolean isPurple) {
        this.setX(startingX);
        this.setY(startingY);
        if (isPurple) this.setImage(Images.PURPLE.getImg());
        else this.setImage(Images.YELLOW.getImg());
        this.isPurple = isPurple;
        this.HP = 3;
    }

    public void hit (Pane parent, boolean isTerminal, boolean isMute) {
        if (isTerminal) HP = 0;
        else HP--;
        if (HP > 0) return;
        Game.getInstance().getAllMiniBosses().remove(MiniBoss.this);
        if (this.getX() > -10 && !isMute)
            Audios.valueOf("MINI_BOSS_DEATH_" + new Random().nextInt(4)).getAudioClip().play();
        MiniBossDeathAnimation animation = new MiniBossDeathAnimation(this);
        animation.play();
        animation.setOnFinished(event -> parent.getChildren().remove(MiniBoss.this));
    }
}
