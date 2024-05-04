package entities.logic;

import constants.Constants;
import maps.logic.Map;

import java.awt.geom.Rectangle2D;

public class BigOrc extends Entity{

    private float leftBoundary;
    private float rightBoundary;
    private float speed;
    boolean right;
    private final static float HORIZONTAL_OFFSET = 20 * Constants.TILE_SCALE; //needed because
    private int health = 100;
    private int maxHealth = 100;
    private boolean isScoreIncreased = false;

    private static int nextId = 0;
    private int id;


    public BigOrc(float x, float y, float leftBoundary, float rightBoundary, float speed) {
        super(x, y - HORIZONTAL_OFFSET, new Rectangle2D.Float(x, y - HORIZONTAL_OFFSET,(96 - 32) * Constants.TILE_SCALE,(96 - 45) * Constants.TILE_SCALE));
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.speed = speed;
        this.right = true;
        this.id = nextId++;
    }

    @Override
    void updatePushback() {
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void update(Map map) {
        if(right && x < rightBoundary) {
            updateXPos(map, speed);
        }
        else if(!right && x > leftBoundary) {
            updateXPos(map, -speed);
        }
        else {
            right = !right;
        }
    }

    private void updateXPos(Map map, float by_value) {
        if(!checkIfPlayerCanMoveToPosition(map, hitbox.x + by_value, hitbox.y, hitbox.width, hitbox.height)) return;
        x += by_value;
        hitbox.x += by_value;
    }

    public void updateSpawnPoint (int x, int y) {
        this.x = x;
        this.y = y - HORIZONTAL_OFFSET;
        this.hitbox.x = x;
        this.hitbox.y = y - HORIZONTAL_OFFSET;
    }

    public boolean checkForCollisonOnPosition(Map map, float x, float y) {
        if(x < 0) return true;
        if(y < 0) return true;

        //TODO: Constants for Tile width and height
        int[][] mapData = map.getMapData();
        int tile_x = (int) (x / 64);
        int tile_y = (int) (y / 64);
        return mapData[tile_y][tile_x] != 11;
    }

    private boolean checkIfPlayerCanMoveToPosition(Map map, float x, float y, float width, float height) {
        if(checkIfPlayerCollidesOverHim(map, x, y, width))
            return false;
        return !checkIfPlayerCollidesUnderHim(map, x, y, width, height);
    }

    private boolean checkIfPlayerCollidesUnderHim(Map map, float x, float y, float width, float height) {
        if (!checkForCollisonOnPosition(map, x,y + height))
            if (!checkForCollisonOnPosition(map, x+width,y+height))
                return false;
        return true;
    }

    private boolean checkIfPlayerCollidesOverHim(Map map, float x, float y, float width) {
        if (!checkForCollisonOnPosition(map, x,y))
            if (!checkForCollisonOnPosition(map, x+width,y))
                return false;
        return true;
    }

    public boolean isRight() {
        return right;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void resetHealth() {
        this.health = this.maxHealth;
    }

    public void setScoreIncreased(boolean isScoreIncreased) {
        this.isScoreIncreased = isScoreIncreased;
    }

    public boolean isScoreIncreased() {
        return isScoreIncreased;
    }

    public int getId() {
        return id;
    }
}
