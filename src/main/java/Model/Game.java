package Model;

import View.Components.*;

import java.util.ArrayList;

public class Game {
    private static Game instance;

    public static Game getInstance() {
        if (instance == null)
            instance = new Game(Database.getInstance().getLoggedInUser());
        return instance;
    }

    private final User player;
    private final Plane plane;
    private final Boss boss;

    private final ArrayList<Bullet> allBullets;
    private final ArrayList <MiniBoss> allMiniBosses;
    private final ArrayList <Bomb> allBombs;

    private Game (User player) {
        this.player = player;
        this.plane = new Plane();
        this.boss = new Boss();
        this.allBullets = new ArrayList<>();
        this.allMiniBosses = new ArrayList<>();
        this.allBombs = new ArrayList<>();
    }

    public Boss getBoss() {
        return boss;
    }

    public Plane getPlane() {
        return plane;
    }

    public User getPlayer() {
        return player;
    }

    public ArrayList<Bullet> getAllBullets() {
        return allBullets;
    }

    public ArrayList<MiniBoss> getAllMiniBosses() {
        return allMiniBosses;
    }

    public ArrayList<Bomb> getAllBombs() {
        return allBombs;
    }

    public static void setInstance (Game Null) {
        instance = Null;
    }
}
