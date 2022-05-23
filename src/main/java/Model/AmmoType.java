package Model;

public enum AmmoType {
    BOMB(360, "/Images/Game/Bomb/icon.png"), BULLET(65, "/Images/Game/Bullet/icon.png");

    private final int delay;
    private final String url;

    AmmoType (int delay, String url) {
        this.delay = delay;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getDelay() {
        return delay;
    }
}
