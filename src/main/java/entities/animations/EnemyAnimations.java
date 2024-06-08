package entities.animations;

import entities.ui.EnemyUI;

public enum EnemyAnimations {
    IDLE, RUN, ATTACK, DEAD;

    public int getAniIndex() {
        return switch (this) {
            case IDLE -> 0;
            case RUN -> 1;
            case ATTACK -> 2;
            case DEAD -> 3;
        };
    }

    public int getAniSize(EnemyUI enemyUI) {
        return switch (this) {
            case IDLE -> enemyUI.getIdleAniSize();
            case RUN -> enemyUI.getRunAniSize();
            case DEAD -> enemyUI.getDeadAniSize();
            case ATTACK -> enemyUI.getAttackAniSize();
        };
    }
}
