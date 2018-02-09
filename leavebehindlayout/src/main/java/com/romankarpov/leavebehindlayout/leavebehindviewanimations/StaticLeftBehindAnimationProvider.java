package com.romankarpov.leavebehindlayout.leavebehindviewanimations;


public class StaticLeftBehindAnimationProvider implements LeftBehindViewAnimationProvider {
    private static StaticLeftBehindAnimationProvider sInstance;
    private StaticLeftBehindViewAnimation mAnimation;

    private StaticLeftBehindAnimationProvider() {
        mAnimation = new StaticLeftBehindViewAnimation();
    }

    public static StaticLeftBehindAnimationProvider get() {
        if (sInstance == null) {
            sInstance = new StaticLeftBehindAnimationProvider();
        }
        return sInstance;
    }

    @Override
    public LeftBehindViewAnimation getLeftAnimation() {
        return mAnimation;
    }
    @Override
    public LeftBehindViewAnimation getTopAnimation() {
        return mAnimation;
    }
    @Override
    public LeftBehindViewAnimation getRightAnimation() {
        return mAnimation;
    }
    @Override
    public LeftBehindViewAnimation getBottomAnimation() {
        return mAnimation;
    }
}
