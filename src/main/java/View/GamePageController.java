package View;

import Application.App;
import Model.AmmoType;
import Model.Game;
import View.Components.Bomb;
import View.Components.Bullet;
import View.Components.Egg;
import View.Components.MiniBoss;
import View.Transitions.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Random;

public class GamePageController implements DefaultAnimation {
    public Pane background;
    public ImageView ammoIcon;

    private final Random random = new Random(0);
    private boolean spacePressed;
    private ZonedDateTime spacePressedTime = null;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;
    private AmmoType ammo;

    private Rectangle bossHealth;

    public void initialize() {
        background.getChildren().add(Game.getPlane());
        background.getChildren().add(Game.getBoss());

        ammo = AmmoType.BULLET;
        ammoIcon = new ImageView(new Image(ammo.getUrl()));
        ammoIcon.setFitWidth(80);
        ammoIcon.setFitHeight(80);
        ammoIcon.setX(5);
        ammoIcon.setY(715);
        background.getChildren().add(ammoIcon);

        background.getChildren().get(0).setOnKeyPressed(event -> setBooleans(event.getCode().toString(), true));

        background.getChildren().get(0).setOnKeyReleased(event -> setBooleans(event.getCode().toString(), false));

        background.setOnMouseClicked(event -> System.out.println("x = " + event.getSceneX() + ", y = " + event.getSceneY()));

        Region bossHealthBar = new Region();
        bossHealthBar.setLayoutX(550);
        bossHealthBar.setLayoutY(5);
        bossHealthBar.setPrefWidth(500);
        bossHealthBar.setPrefHeight(25);
        bossHealthBar.setStyle("-fx-border-radius: 5; -fx-border-width: 3; -fx-border-color: white; -fx-fill: none;");
        background.getChildren().add(bossHealthBar);

        bossHealth = new Rectangle();
        bossHealth.setX(552);
        bossHealth.setY(7.5);
        bossHealth.setWidth(495);
        bossHealth.setHeight(20);
        bossHealth.setArcWidth(10);
        bossHealth.setArcHeight(10);
        bossHealth.setFill(Color.rgb(214, 44, 71, 1));
        background.getChildren().add(bossHealth);

        App.getTimeLine(background, this).play();
    }

    private void managePlane() {
        background.getChildren().get(background.getChildren().indexOf(Game.getPlane())).requestFocus();
        if (upPressed) Game.getPlane().moveUp();
        if (downPressed) Game.getPlane().moveDown();
        if (rightPressed) Game.getPlane().moveRight();
        if (leftPressed) Game.getPlane().moveLeft();
        if (spacePressed && (spacePressedTime == null ||
                Duration.between(spacePressedTime, ZonedDateTime.now()).toMillis() / ammo.getDelay() > 1)) {
            fireAmmo();
            spacePressedTime = ZonedDateTime.now();
        }
    }

    private void setBooleans (String decision, boolean status) {
        if (decision.equals("SHIFT") && status) {
            if (ammo.equals(AmmoType.BOMB)) ammo = AmmoType.BULLET;
            else ammo = AmmoType.BOMB;
            ammoIcon.setImage(new Image(ammo.getUrl()));
        }

        if (decision.equals("UP") || decision.equals("W")) {
            if (upPressed != status) new PlaneAnimation("Up", status).play();
            upPressed = status;
        }
        if (decision.equals("DOWN") || decision.equals("S")) {
            if (downPressed != status) new PlaneAnimation("Down", status).play();
            downPressed = status;
        }
        if (decision.equals("LEFT") || decision.equals("A")) leftPressed = status;
        if (decision.equals("RIGHT") || decision.equals("D")) rightPressed = status;
        if (decision.equals("SPACE")) spacePressed = status;
    }

    private void fireAmmo() {
        if (ammo.equals(AmmoType.BULLET)) {
            Bullet bullet = new Bullet();
            background.getChildren().add(bullet);
            Game.getAllBullets().add(bullet);
            bullet.fire(background);
        }
        else {
            Bomb bomb = new Bomb();
            background.getChildren().add(bomb);
            Game.getAllBombs().add(bomb);
            bomb.fire();
        }
    }

    private void spawnMiniBossLine() {
        int startingY = (int)(Math.random() * (600));
        for (int i = 0; i < 5; i++) {
            MiniBoss miniBoss = new MiniBoss(1700 + 200 * i, startingY, random.nextBoolean());
            background.getChildren().add(miniBoss);
            Game.getAllMiniBosses().add(miniBoss);
            MiniBossMovementAnimation miniBossMovementAnimation = new MiniBossMovementAnimation(miniBoss, background);
            miniBossMovementAnimation.play();
        }
    }

    private boolean noMiniBossOnMap() {
        background.getChildren().removeIf(child -> child instanceof MiniBoss && ((MiniBoss) child).getX() < -150);

        for (Node child : background.getChildren())
            if (child instanceof MiniBoss)
                return false;
        return true;
    }

    private void handleCollisions() {
        for (int i = 0; i < Game.getAllBombs().size(); i++)
            if (bombIntersectsBoss(Game.getAllBombs().get(i), Game.getBoss())) {
                Game.getBoss().getBombed();
                changeBossStatus();
                Game.getAllBombs().get(i).explode(background);
                updateHealthBar();
                i--;
            }

        for (int i = 0; i < Game.getAllBullets().size(); i++)
            if (bulletIntersectsBoss(Game.getAllBullets().get(i), Game.getBoss())) {
                Game.getBoss().getShot();
                changeBossStatus();
                Game.getAllBullets().get(i).explode(background);
                updateHealthBar();
                i--;
            }

        for (Node child : background.getChildren()) {
            if (!(child instanceof Egg)) continue;
            if (child.intersects(Game.getPlane().getBoundsInParent())) {
                Game.getPlane().hitEgg();
                background.getChildren().removeIf(node -> node instanceof Egg);
                break;
            }
        }

        for (int i = 0; i < Game.getAllBullets().size(); i++) {
            for (MiniBoss miniBoss : Game.getAllMiniBosses()) {
                if (miniBoss.intersects(Game.getAllBullets().get(i).getBoundsInParent())) {
                    miniBoss.hit(background, false);
                    Game.getAllBullets().get(i).explode(background);
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i < Game.getAllMiniBosses().size(); i++)
            if (Game.getAllMiniBosses().get(i).intersects(Game.getPlane().getBoundsInParent())) {
                Game.getAllMiniBosses().get(i).hit(background, true);
                Game.getPlane().hitMiniBoss();
                i--;
            }

        for (int i = 0; i < Game.getAllBombs().size(); i++) {
            for (MiniBoss miniBoss : Game.getAllMiniBosses()) {
                if (miniBoss.intersects(Game.getAllBombs().get(i).getBoundsInParent())) {
                    miniBoss.hit(background, true);
                    Game.getAllBombs().get(i).explode(background);
                    i--;
                    break;
                }
            }
        }
    }

    private void updateHealthBar() {
        bossHealth.setWidth(Game.getBoss().getHealth() * 2.5 - 5);
    }

    private void changeBossStatus() {
        if (Game.getBoss().getHealth() <= 0) {
            Game.getBoss().getBossAnimation().stop();
            new BossDeathAnimation().play();
        }
        else if (Game.getBoss().getHealth() % 49 == 0) {
            Game.getBoss().getBossAnimation().stop();

            BossEggAttackAnimation animation = new BossEggAttackAnimation(background);

            animation.play();
        }
    }

    @Override
    public void setTimeLine(Pane pane, double xPosition) {
        managePlane();

        if (noMiniBossOnMap() && random.nextBoolean() && random.nextBoolean())
            spawnMiniBossLine();

        handleCollisions();

        String style = "-fx-background-position: " +
                "left " + xPosition * 1.2 * 3 + "px top," +
                "left " + xPosition * 1.7 * 3 + "px top," +
                "left " + xPosition * 2.3 * 3 + "px bottom," +
                "left " + xPosition * 2.7 * 3 + "px bottom," +
                "left " + xPosition * 3 * 3 + "px bottom," +
                "left " + xPosition * 3.8 * 3 + "px bottom," +
                "left " + xPosition * 4 * 3 + "px bottom," +
                "left " + xPosition * 4.5 * 3 + "px bottom;";
        pane.setStyle(style);
    }

    //OverRide... kinda

    private boolean bulletIntersectsBoss(ImageView bullet, ImageView boss) {
        if (!bullet.intersects(boss.getBoundsInParent()) || bullet.getY() < 223) return false;

        if (bullet.getY() < 321) {
            double minX = (-57 * bullet.getY()) / 64 + 1355 - 40;
            return bullet.getX() > minX;
        }

        PixelReader bossPixelReader = boss.getImage().getPixelReader();

        int intersections = 0;

        for (int y = (int)(bullet.getY() - boss.getLayoutY()); y < boss.getImage().getHeight(); y++) {
            if (y < 0) continue;
            int x = (int)(bullet.getX() - boss.getLayoutX() - 10);
            if (x < 0) continue;
            Color pxColor = bossPixelReader.getColor(x, y);
            if (pxColor.getOpacity() == 1)
                intersections++;
        }

        return intersections > bullet.getImage().getHeight() / 2.5;
    }

    private boolean bombIntersectsBoss (ImageView bomb, ImageView boss) {

        if (!bomb.intersects(boss.getBoundsInParent()) || bomb.getY() > 480) return false;

        double minY = -1.2 * bomb.getX() + 1501;

        return minY < bomb.getY();
    }

}
