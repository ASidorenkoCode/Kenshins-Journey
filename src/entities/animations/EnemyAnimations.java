package entities.animations;

public enum EnemyAnimations {
    IDLE, RUN, ATTACK;

    public int getAniIndex() {
        return switch (this) {
            case IDLE -> 0;
            case RUN -> 1;
            case ATTACK -> 2;
        };
    }

    public int getAniSize() {
        return switch (this) {
            case IDLE, RUN -> 4;
            case ATTACK -> 8;
        };
    }
}
