package items.animations;

public enum ItemAnimations {
    HEART, POWER_RING;

    public int getAniIndex() {
        return switch (this) {
            case HEART -> 0;
            case POWER_RING -> 1;
        };
    }

    public int getAniSize() {
        return switch (this) {
            case HEART -> 7;
            case POWER_RING -> 7;
        };
    }
}
