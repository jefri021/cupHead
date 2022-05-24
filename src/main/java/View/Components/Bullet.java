package View.Components;

import Model.Game;
import View.Transitions.BulletExplosionAnimation;
import View.Transitions.BulletFireAnimation;
import View.Transitions.BulletMovementAnimation;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bullet extends ImageView {

    public void move (double dx) {
        this.setX(this.getX() + dx);
    }

    BulletFireAnimation fireAnimation;
    BulletMovementAnimation movementAnimation;

    public void fire (Pane parent) {
        fireAnimation = new BulletFireAnimation(this);
        fireAnimation.play();
        fireAnimation.setOnFinished(event -> {
            ObservableList <Node> children = Bullet.this.getParent().getChildrenUnmodifiable();
            Bullet.this.setImage(new Image("/Images/Game/Bullet/Travel/0.png"));
            Bullet.this.setFitWidth(72);
            Bullet.this.setFitHeight(15);
            double planeX = ((ImageView)(children.get(children.indexOf(Game.getPlane())))).getX(),
                    planeY = ((ImageView)(children.get(children.indexOf(Game.getPlane())))).getY();
            Bullet.this.setX(planeX + 90);
            Bullet.this.setY(planeY + 35);
            movementAnimation = new BulletMovementAnimation(Bullet.this, parent);
            movementAnimation.play();
            movementAnimation.setOnFinished(event1 -> {
                Game.getAllBullets().remove(Bullet.this);
                parent.getChildren().remove(Bullet.this);
            });
        });
    }

    public void explode (Pane parent) {
        if (fireAnimation != null) fireAnimation.stop();
        if (movementAnimation != null) movementAnimation.stop();
        BulletExplosionAnimation animation = new BulletExplosionAnimation(this);
        animation.play();
        Game.getAllBullets().remove(this);
        animation.setOnFinished(event -> parent.getChildren().remove(this));
    }
}
