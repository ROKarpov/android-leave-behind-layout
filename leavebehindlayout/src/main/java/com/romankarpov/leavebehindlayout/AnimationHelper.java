package com.romankarpov.leavebehindlayout;

class AnimationHelper {
    public static final int STATIC_ANIMATION = 1;
    public static final int SLIDE_ANIMATION = 2;

    public static final int DEFAULT_ANIMATION = STATIC_ANIMATION;

    static public LeftBehindViewAnimation getAnimation(int flag) {
        switch (flag) {
            case STATIC_ANIMATION:
                return StaticLeftBehindViewAnimation.get();
            case SLIDE_ANIMATION:
                return SlideLeftBehindViewAnimation.get();
            default:
                throw new IllegalStateException("Unsupportable animation flag.");
        }
    }
}
