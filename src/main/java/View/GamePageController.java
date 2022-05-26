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

    private void initEverything() {
        Game.setInstance(null);
        Media mainTheme = new Media(this.getClass().getResource("/Audios/Game/main_theme.mp3").toString());
        mainThemePlayer = new MediaPlayer(mainTheme);
        mainThemePlayer.setCycleCount(-1);
        mainThemePlayer.play();

        random = new Random(0);
        spacePressed = leftPressed = rightPressed = downPressed = upPressed = false;
        ammo = AmmoType.BULLET;
        timeLabel = new Label();
        startTime = Clock.systemUTC().millis();
        totalMiniBossKill = 0;
        background.getChildren().add(Game.getInstance().getPlane());
        background.getChildren().add(Game.getInstance().getBoss());

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
        background.getChildren().add(Game.getInstance().getPlane().getHealthBar());
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
        background.getChildren().get(background.getChildren().indexOf(Game.getInstance().getPlane())).requestFocus();
        if (upPressed) Game.getInstance().getPlane().moveUp();
        if (downPressed) Game.getInstance().getPlane().moveDown();
        if (rightPressed) Game.getInstance().getPlane().moveRight();
        if (leftPressed) Game.getInstance().getPlane().moveLeft();
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
            bullet.fire(background);
            background.getChildren().add(bullet);
            Game.getInstance().getAllBullets().add(bullet);
            if (!mainThemePlayer.isMute()) Audios.BULLET.getAudioClip().play();
        }
        else {
            Bomb bomb = new Bomb();
            bomb.fire();
            background.getChildren().add(bomb);
            Game.getInstance().getAllBombs().add(bomb);
        }
    }

    private void spawnMiniBossLine() {
        for (int i = 0; i < 5; i++) {
            MiniBoss miniBoss = new MiniBoss(1700 + 200 * i, random.nextInt(600), random.nextBoolean());
            background.getChildren().add(miniBoss);
            Game.getInstance().getAllMiniBosses().add(miniBoss);
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

        if (planeIntersectsBoss() && Game.getInstance().getPlane().getOpacity() == 1)
            Game.getInstance().getPlane().hit("boss");

        for (int i = 0; i < Game.getInstance().getAllBombs().size(); i++)
            if (bombIntersectsBoss(Game.getInstance().getAllBombs().get(i), Game.getInstance().getBoss())) {
                Game.getInstance().getBoss().getBombed();
                changeBossStatus();
                Game.getInstance().getAllBombs().get(i).explode(background);
                updateHealthBar();
                i--;
            }

        for (int i = 0; i < Game.getInstance().getAllBullets().size(); i++)
            if (bulletIntersectsBoss(Game.getInstance().getAllBullets().get(i), Game.getInstance().getBoss())) {
                Game.getInstance().getBoss().getShot();
                changeBossStatus();
                Game.getInstance().getAllBullets().get(i).explode(background);
                updateHealthBar();
                i--;
            }

        for (int i = 0; i < background.getChildren().size(); i++) {
            Node child = background.getChildren().get(i);
            if (!(child instanceof Egg)) continue;
            if (child.intersects(Game.getInstance().getPlane().getBoundsInParent()) && Game.getInstance().getPlane().getOpacity() == 1) {
                Game.getInstance().getPlane().hit("egg");

                if (! mainThemePlayer.isMute())
                    Audios.valueOf("EGG_" + random.nextInt(4)).getAudioClip().play();

                background.getChildren().remove(child);
                i--;
            }
            else if (((Egg) child).getX() < -50) {
                background.getChildren().remove(child);
                i--;
            }
        }

        for (int i = 0; i < Game.getInstance().getAllBullets().size(); i++) {
            for (MiniBoss miniBoss : Game.getInstance().getAllMiniBosses()) {
                if (miniBoss.intersects(Game.getInstance().getAllBullets().get(i).getBoundsInParent())) {
                    miniBoss.hit(background, false, mainThemePlayer.isMute());
                    Game.getInstance().getAllBullets().get(i).explode(background);
                    totalMiniBossKill++;
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i < Game.getInstance().getAllMiniBosses().size(); i++)
            if (Game.getInstance().getAllMiniBosses().get(i).intersects(Game.getInstance().getPlane().getBoundsInParent()) &&
                Game.getInstance().getPlane().getOpacity() == 1) {
                Game.getInstance().getAllMiniBosses().get(i).hit(background, true, mainThemePlayer.isMute());
                Game.getInstance().getPlane().hit("miniBoss");
                i--;
            }

        for (int i = 0; i < Game.getInstance().getAllBombs().size(); i++) {
            for (MiniBoss miniBoss : Game.getInstance().getAllMiniBosses()) {
                if (miniBoss.intersects(Game.getInstance().getAllBombs().get(i).getBoundsInParent())) {
                    miniBoss.hit(background, true, mainThemePlayer.isMute());
                    Game.getInstance().getAllBombs().get(i).explode(background);
                    totalMiniBossKill++;
                    i--;
                    break;
                }
            }
        }

        if (Game.getInstance().getPlane().getHealth() <= 0) endGame();

    }

    private void updateHealthBar() {
        bossHealthBar.setProgress(Game.getInstance().getBoss().getHealth() / 600);
        bossHealthLabel.setText(""+(int)Game.getInstance().getBoss().getHealth());
    }

    private void changeBossStatus() {
        if (Game.getInstance().getBoss().getHealth() <= 0) {
            Game.getInstance().getBoss().getBossAnimation().stop();
            BossDeathAnimation animation = new BossDeathAnimation();
            animation.play();
            animation.setOnFinished(event -> endGame());
            timeLine.stop();
            timeLine.getKeyFrames().clear();
            timeLine = null;
        }
        else if (Game.getInstance().getBoss().getHealth() % ((int)(Game.getInstance().getBoss().getHealth() / 20) + 10) == 0) {
            Game.getInstance().getBoss().getBossAnimation().stop();

            BossBarfAnimation animation = new BossBarfAnimation(background);

            animation.play();

            if (! mainThemePlayer.isMute())
                Audios.valueOf("SPIT_" + random.nextInt(5)).getAudioClip().play();
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
        if (!bullet.intersects(boss.getBoundsInParent()) || bullet.getImage() == null) return false;

        PixelReader bossPixelReader = boss.getImage().getPixelReader();

        int x = (int)(bullet.getX() + bullet.getImage().getWidth() - boss.getX() - 100);
        if (x < 0) return false;

        for (int y = (int)(bullet.getY() - boss.getY()); y < (int)(bullet.getY() - boss.getY()) + bullet.getImage().getHeight(); y++) {
            try {
                Color pxColor = bossPixelReader.getColor(x, y);
                if (pxColor.getOpacity() == 1)
                    return true;
            }
            catch (Exception ignored) {
            }
        }
        return false;
    }

    private boolean bombIntersectsBoss (ImageView bomb, ImageView boss) {

        if (!bomb.intersects(boss.getBoundsInParent()) || bomb.getY() > 480) return false;

        double minY = -1.2 * bomb.getX() + 1501;

        return minY < bomb.getY();
    }

    private boolean planeIntersectsBoss() {
        if (!Game.getInstance().getPlane().intersects(Game.getInstance().getBoss().getBoundsInParent())) return false;

        double dist = Math.sqrt(Math.pow(Game.getInstance().getBoss().getX() - Game.getInstance().getPlane().getX(), 2) +
                                Math.pow(Game.getInstance().getBoss().getY() - Game.getInstance().getPlane().getY(), 2));
        return dist < 500;
    }

    private void endGame() {
        timeLine.stop();
        timeLine.getKeyFrames().clear();
        mainThemePlayer.stop();
        Game.getInstance().getBoss().getBossAnimation().stop();

        if (Game.getInstance().getPlayer() == null) {
            App.changePage("LoginAndRegistrationPage");
            return;
        }

        int score = 0, time = (int)(Clock.systemUTC().millis() - startTime) / 1000;
        if (Game.getInstance().getBoss().getHealth() <= 0 && time < 120) score = (int)(1.1 * (120 - time));

        score += totalMiniBossKill;
        Game.getInstance().getPlayer().updateScore(score);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        background.setEffect(colorAdjust);

        ImageView quoteCard;
        if (Game.getInstance().getBoss().getHealth() <= 0) quoteCard = new ImageView(new Image("/Images/Game/win.png"));
        else {
            quoteCard = new ImageView(new Image("/Images/Game/lose.png"));
            Audios.LOSE.getAudioClip().play();
        }
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
        planeProgress.setX(-0.662 * Game.getInstance().getBoss().getHealth() + 970);
        planeProgress.setY(470);
        background.getChildren().add(planeProgress);

        background.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getSceneX() > 753 && mouseEvent.getSceneX() < 846 &&
                mouseEvent.getSceneY() > 568 && mouseEvent.getSceneY() < 591) {
                App.changePage("GamePage");
            }
            if (mouseEvent.getSceneX() > 709 && mouseEvent.getSceneX() < 886 &&
                    mouseEvent.getSceneY() > 656 && mouseEvent.getSceneY() < 679) {
                App.changePage("MainMenuPage");
            }
        });
    }
}
