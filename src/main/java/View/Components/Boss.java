package View.Components;

import View.Transitions.BossAnimation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Boss extends ImageView {

    private final BossAnimation bossAnimation;

    public BossAnimation getBossAnimation() {
        return bossAnimation;
    }

    private double health;

    public Boss() {
        this.setImage(new Image("/Images/Game/Boss/0.gif"));
        this.setX(900);
        this.setY(150);
        this.health = 600;
        bossAnimation = new BossAnimation();
        bossAnimation.play();
    }

    public double getHealth() {
        return health;
    }

    public void getShot() {
        this.health--;
    }

    public void getBombed() {
        this.health -= 10;
    }
}
