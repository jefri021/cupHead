package View;

import Application.App;
import Model.AmmoType;
import Model.Game;
import View.Components.Bomb;
import View.Components.Bullet;
import View.Components.Egg;
import View.Components.MiniBoss;
import View.Transitions.*;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Random;

public class GamePageController implements DefaultAnimation {
    public Pane background;
    public ImageView ammoIcon;
    private Timeline timeLine;

    private Random random = new Random(0);
    private boolean spacePressed;
    private ZonedDateTime spacePressedTime = null;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean upPressed;
    private boolean downPressed;
    private AmmoType ammo;
    private Label timeLabel;

    private long startTime;
    private int totalMiniBossKill;
    private boolean isGrayscale;
    private ProgressBar bossHealthBar;
    private Label bossHealthLabel;

    private MediaPlayer mainThemePlayer;
    private Media[] audios;

    private void initEverything() {
        Media mainTheme = new Media(this.getClass().getResource("/Audios/Game/main_theme.mp3").toString());
        mainThemePlayer = new MediaPlayer(mainTheme);
        mainThemePlayer.play();

        audios = new Media[9];
        for (int i = 0; i < 5; i++)
            audios[i] = new Media(this.getClass().getResource("/Audios/Game/spit" + i + ".wav").toString());
        for (int i = 5; i < 9; i++)
            audios[i] = new Media(this.getClass().getResource("/Audios/Game/egg" + (i - 5) + ".wav").toString());

        random = new Random(0);
        spacePressed = leftPressed = rightPressed = downPressed = upPressed = false;
        ammo = AmmoType.BULLET;
        timeLabel = new Label();
        startTime = Clock.systemUTC().millis();
        totalMiniBossKill = 0;
        background.getChildren().add(Game.getPlane());
        background.getChildren().add(Game.getBoss());

        ammoIcon = new ImageView(new Image(ammo.getUrl()));
        ammoIcon.setFitWidth(80);
        ammoIcon.setFitHeight(80);
        ammoIcon.setX(5);
        ammoIcon.setY(715);
        background.getChildren().add(ammoIcon);

        bossHealthBar = new ProgressBar();
        bossHealthBar.setLayoutX(550);
        bossHealthBar.setLayoutY(10);
        bossHealthBar.setPrefWidth(500);
        bossHealthBar.setProgress(1);
        bossHealthBar.setStyle("-fx-accent: #d00000;");

        bossHealthLabel = new Label();
        bossHealthLabel.setLayoutX(780);
        bossHealthLabel.setLayoutY(3);
        bossHealthLabel.setStyle("-fx-font-family: 'Macondo'; -fx-font-weight: bold; -fx-font-size: 25; -fx-text-fill: black;");
        bossHealthLabel.setText("600");

        background.getChildren().add(bossHealthBar);
        background.getChildren().add(Game.getPlane().getHealthBar());
        background.getChildren().add(bossHealthLabel);

        timeLabel = new Label();
        background.getChildren().add(timeLabel);

        isGrayscale = false;
    }

    public void initialize() {
        initEverything();

        background.getChildren().get(0).setOnKeyPressed(event -> handleKeyEvents(event.getCode().toString(), true));

        background.getChildren().get(0).setOnKeyReleased(event -> handleKeyEvents(event.getCode().toString(), false));

        timeLine = App.getTimeLine(background, this);
        timeLine.play();
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

    private void handleKeyEvents (String decision, boolean status) {

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

        if (decision.equals("TAB") && status) isGrayscale = !isGrayscale;

        if (decision.equals("M") && status)
            mainThemePlayer.setMute(!mainThemePlayer.isMute());
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
        if (Game.getPlane().getY() < 290 || Game.getPlane().getY() > 500) startingY = (int)Game.getPlane().getY();
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

        if (Game.getPlane().intersects(Game.getBoss().getBoundsInParent()) && Game.getPlane().getOpacity() == 1)
            Game.getPlane().hit("boss");

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

        for (int i = 0; i < background.getChildren().size(); i++) {
            Node child = background.getChildren().get(i);
            if (!(child instanceof Egg)) continue;
            if (child.intersects(Game.getPlane().getBoundsInParent()) && Game.getPlane().getOpacity() == 1) {
                Game.getPlane().hit("egg");
                if (! mainThemePlayer.isMute()) {
                    MediaPlayer mediaPlayer = new MediaPlayer(audios[random.nextInt(4) + 5]);
                    mediaPlayer.play();
                }
                background.getChildren().remove(child);
                i--;
            }
            else if (((Egg) child).getX() < -50) {
                background.getChildren().remove(child);
                i--;
            }
        }

        for (int i = 0; i < Game.getAllBullets().size(); i++) {
            for (MiniBoss miniBoss : Game.getAllMiniBosses()) {
                if (miniBoss.intersects(Game.getAllBullets().get(i).getBoundsInParent())) {
                    miniBoss.hit(background, false);
                    Game.getAllBullets().get(i).explode(background);
                    totalMiniBossKill++;
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i < Game.getAllMiniBosses().size(); i++)
            if (Game.getAllMiniBosses().get(i).intersects(Game.getPlane().getBoundsInParent()) &&
                Game.getPlane().getOpacity() == 1) {
                Game.getAllMiniBosses().get(i).hit(background, true);
                Game.getPlane().hit("miniBoss");
                i--;
            }

        for (int i = 0; i < Game.getAllBombs().size(); i++) {
            for (MiniBoss miniBoss : Game.getAllMiniBosses()) {
                if (miniBoss.intersects(Game.getAllBombs().get(i).getBoundsInParent())) {
                    miniBoss.hit(background, true);
                    Game.getAllBombs().get(i).explode(background);
                    totalMiniBossKill++;
                    i--;
                    break;
                }
            }
        }

        if (Game.getPlane().getHealth() <= 0) endGame();

    }

    private void updateHealthBar() {
        bossHealthBar.setProgress(Game.getBoss().getHealth() / 600);
        bossHealthLabel.setText(""+(int)Game.getBoss().getHealth());
    }

    private void changeBossStatus() {
        if (Game.getBoss().getHealth() <= 0) {
            Game.getBoss().getBossAnimation().stop();
            BossDeathAnimation animation = new BossDeathAnimation();
            animation.play();
            animation.setOnFinished(event -> endGame());
            timeLine.stop();
        }
        else if (Game.getBoss().getHealth() % 10 == 0 && Game.getPlane().getY() > 200 && Game.getPlane().getY() < 600) {
            Game.getBoss().getBossAnimation().stop();

            BossEggAttackAnimation animation = new BossEggAttackAnimation(background);

            animation.play();

            if (! mainThemePlayer.isMute()) {
                MediaPlayer mediaPlayer = new MediaPlayer(audios[random.nextInt(5)]);
                mediaPlayer.play();
            }
        }
    }

    @Override
    public void setTimeLine(Pane pane, double xPosition) {
        long time = (Clock.systemUTC().millis() - startTime) / 1000;
        timeLabel.setText(Long.toString(time / 60) + ':' + (time % 60));
        timeLabel.setLayoutX(20);
        timeLabel.setLayoutY(680);
        timeLabel.setStyle("-fx-font-family: 'Macondo'; -fx-font-size: 35; -fx-font-weight: bold;");

        handleGrayscale();

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

    private void handleGrayscale() {
        double saturation = 0;
        double brightness = 0;
        if (isGrayscale) {
            saturation = -1;
            brightness = -0.2;
        }
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(saturation);
        colorAdjust.setBrightness(brightness);
        background.setEffect(colorAdjust);
    }

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

    private void endGame() {
        mainThemePlayer.stop();
        timeLine.stop();
        if (Game.getPlayer() == null) {
            App.changePage("LoginAndRegistrationPage");
            return;
        }

        int score = 0, time = (int)(Clock.systemUTC().millis() - startTime) / 1000;
        if (Game.getBoss().getHealth() <= 0 && time < 120) score = (int)(1.1 * (120 - time));
        score += totalMiniBossKill;
        Game.getPlayer().updateScore(score);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        background.setEffect(colorAdjust);

        ImageView quoteCard;
        if (Game.getBoss().getHealth() <= 0) quoteCard = new ImageView(new Image("/Images/Game/win.png"));
        else quoteCard = new ImageView(new Image("/Images/Game/lose.png"));
        quoteCard.setX(550);
        quoteCard.setY(100);
        quoteCard.setFitWidth(500);
        quoteCard.setFitHeight(680);
        Rectangle clip = new Rectangle();
        clip.setX(quoteCard.getX());
        clip.setY(quoteCard.getY());
        clip.setWidth(quoteCard.getFitWidth());
        clip.setHeight(quoteCard.getFitHeight());
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        quoteCard.setClip(clip);
        background.getChildren().add(quoteCard);

        ImageView planeProgress = new ImageView(new Image("/Images/Game/Plane/0.gif"));
        planeProgress.setFitHeight(56);
        planeProgress.setFitWidth(64);
        planeProgress.setX(-0.662 * Game.getBoss().getHealth() + 970);
        planeProgress.setY(470);
        background.getChildren().add(planeProgress);

        background.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getSceneX() > 753 && mouseEvent.getSceneX() < 846 &&
                mouseEvent.getSceneY() > 568 && mouseEvent.getSceneY() < 591) {
                Game.startNewGame(Game.getPlayer());
                App.changePage("GamePage");
            }
            if (mouseEvent.getSceneX() > 709 && mouseEvent.getSceneX() < 886 &&
                    mouseEvent.getSceneY() > 656 && mouseEvent.getSceneY() < 679) {
                App.changePage("MainMenuPage");
            }
        });
    }

}
