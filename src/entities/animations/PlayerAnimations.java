package entities.animations;

public enum PlayerAnimations {
    IDLE, RUN, WALK, RUN_SLASH, IDLE_SLASH, JUMP_SLASH, FALL_SLASH, JUMP, FALL,
    WALL_SLIDE, LANDING, HEAVY_ATTACK, BLOCK, BLOCK_HIT, DASH, RESTING, DEATH;

    public int getAniIndex() {
        return switch (this) {
            case IDLE -> 0;
            case RUN -> 1;
            case WALK -> 2;
            case RUN_SLASH -> 3;
            case IDLE_SLASH -> 4;
            case JUMP_SLASH -> 5;
            case FALL_SLASH -> 6;
            case JUMP -> 7;
            case FALL -> 8;
            case WALL_SLIDE -> 9;
            case LANDING -> 10;
            case HEAVY_ATTACK -> 11;
            case BLOCK -> 12;
            case BLOCK_HIT -> 13;
            case DASH -> 14;
            case RESTING -> 15;
            case DEATH -> 16;
        };
    }

    public int getAniSize() {
        return switch (this) {
            case IDLE, LANDING, RESTING -> 5;
            case RUN, WALK, RUN_SLASH, IDLE_SLASH, JUMP_SLASH, FALL_SLASH -> 8;
            case JUMP, FALL, WALL_SLIDE, BLOCK, BLOCK_HIT -> 4;
            case HEAVY_ATTACK -> 13;
            case DASH -> 6;
            case DEATH -> 17;
        };
    }

    public int getAniSpeed() {
        return switch (this) {
            case IDLE, LANDING, RESTING, JUMP, FALL, WALL_SLIDE, BLOCK, BLOCK_HIT -> 10;
            case RUN, WALK, RUN_SLASH, IDLE_SLASH, JUMP_SLASH, FALL_SLASH, DEATH -> 5;
            case HEAVY_ATTACK, DASH -> 3;
        };
    }
}
