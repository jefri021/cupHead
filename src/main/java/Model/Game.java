package Model;

import View.Components.*;

import java.util.ArrayList;

public class Game {
    private static User player;
    private static Plane plane;
    private static Boss boss;

    private static ArrayList<Bullet> allBullets;
    private static ArrayList <MiniBoss> allMiniBosses;
    private static ArrayList <Bomb> allBombs;

    public static void startNewGame (User player) {
        Game.player = player;
        Game.plane = new Plane();
        Game.boss = new Boss();
        Game.allBullets = new ArrayList<>();
        Game.allMiniBosses = new ArrayList<>();
        Game.allBombs = new ArrayList<>();
    }

    public static Boss getBoss() {
        return boss;
    }

    public static Plane getPlane() {
        return plane;
    }

    public static User getPlayer() {
        return player;
    }

    public static ArrayList<Bullet> getAllBullets() {
        return allBullets;
    }

    public static ArrayList<MiniBoss> getAllMiniBosses() {
        return allMiniBosses;
    }

    public static ArrayList<Bomb> getAllBombs() {
        return allBombs;
    }
}
