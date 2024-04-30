package entities.animations;

public enum PlayerAnimations {
    IDLE, RUN, WALK, RUN_SLASH, IDLE_SLASH, JUMP, FALL,
    WALL_SLIDE, LANDING, HEAVY_ATTACK, BLOCK, BLOCK_HIT, DASH, RESTING, DEATH;

    public int getAniIndex() {
        switch(this) {
            case IDLE: return 0;
            case RUN: return 1;
            case WALK: return 2;
            case RUN_SLASH: return 3;
            case IDLE_SLASH: return  4;
            case JUMP: return 5;
            case FALL: return 6;
            case WALL_SLIDE: return 7;
            case LANDING: return 8;
            case HEAVY_ATTACK: return 9;
            case BLOCK: return 10;
            case BLOCK_HIT: return 11;
            case DASH: return 12;
            case RESTING: return 13;
            case DEATH: return 14;
        }
        return 4; //Default value = 4, because ani has at least 4 pictures
    }

    public int getAniSize() {
        switch(this) {
            case IDLE, LANDING, RESTING: return 5;
            case RUN, WALK, RUN_SLASH, IDLE_SLASH: return 8;
            case JUMP, FALL, WALL_SLIDE, BLOCK, BLOCK_HIT: return 4;
            case HEAVY_ATTACK: return 13;
            case DASH: return 6;
            case DEATH: return 17;
        }
        return 4; //Default value = 4, because ani has at least 4 pictures
    }
}
