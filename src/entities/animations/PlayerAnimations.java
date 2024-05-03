package entities.animations;

public enum PlayerAnimations {
    IDLE, RUN, WALK, RUN_SLASH, IDLE_SLASH, JUMP, FALL,
    WALL_SLIDE, LANDING, HEAVY_ATTACK, BLOCK, BLOCK_HIT, DASH, RESTING, DEATH;

    public int getAniIndex() {
        return switch (this) {
            case IDLE -> 0;
            case RUN -> 1;
            case WALK -> 2;
            case RUN_SLASH -> 3;
            case IDLE_SLASH -> 4;
            case JUMP -> 5;
            case FALL -> 6;
            case WALL_SLIDE -> 7;
            case LANDING -> 8;
            case HEAVY_ATTACK -> 9;
            case BLOCK -> 10;
            case BLOCK_HIT -> 11;
            case DASH -> 12;
            case RESTING -> 13;
            case DEATH -> 14;
        };
    }

    public int getAniSize() {
        return switch (this) {
            case IDLE, LANDING, RESTING -> 5;
            case RUN, WALK, RUN_SLASH, IDLE_SLASH -> 8;
            case JUMP, FALL, WALL_SLIDE, BLOCK, BLOCK_HIT -> 4;
            case HEAVY_ATTACK -> 13;
            case DASH -> 6;
            case DEATH -> 17;
        };
    }
}
