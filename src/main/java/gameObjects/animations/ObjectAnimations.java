package gameObjects.animations;

public enum ObjectAnimations {
    FINISH;

    public int getAniIndex() {
        return switch (this) {
            case FINISH -> 0;
        };
    }

    public int getAniSize() {
        return switch (this) {
            case FINISH -> 7;
        };
    }
}
