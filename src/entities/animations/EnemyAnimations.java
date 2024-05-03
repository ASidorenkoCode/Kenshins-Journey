package entities.animations;

public enum EnemyAnimations {
    IDLE,RUN;

    public int getAniIndex() {
        return switch (this) {
            case IDLE -> 0;
            case RUN -> 1;
        };
    }

    public int getAniSize() {
        return switch (this) {
            case IDLE, RUN -> 4;
        };
    }
}
