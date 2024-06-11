package spriteControl;

public class SpriteData {
    private final String path;
    private final int xDimension;
    private final int idleAniSize;
    private final int runAniSize;
    private final int attackAniSize;
    private final int deadAniSize;
    private final float scale;
    private final float yAdjustmentLiving;
    private final float yAdjustmentDead;

    public SpriteData(String path, int xDimension, int idleAniSize, int runAniSize, int attackAniSize, int deadAniSize, float scale, float yAdjustmentLiving, float yAdjustmentDead) {
        this.path = path;
        this.xDimension = xDimension;
        this.idleAniSize = idleAniSize;
        this.runAniSize = runAniSize;
        this.attackAniSize = attackAniSize;
        this.deadAniSize = deadAniSize;
        this.scale = scale;
        this.yAdjustmentLiving = yAdjustmentLiving;
        this.yAdjustmentDead = yAdjustmentDead;
    }

    public String getPath() {
        return path;
    }

    public int getXDimension() {
        return xDimension;
    }

    public int getIdleAniSize() {
        return idleAniSize;
    }

    public int getRunAniSize() {
        return runAniSize;
    }

    public int getAttackAniSize() {
        return attackAniSize;
    }

    public int getDeadAniSize() {
        return deadAniSize;
    }

    public float getScale() {
        return scale;
    }

    public float getYAdjustmentLiving() {
        return yAdjustmentLiving;
    }

    public float getYAdjustmentDead() {
        return yAdjustmentDead;
    }
}