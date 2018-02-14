package com.romankarpov.leavebehindlayout.leavebehindviewanimations;


public class EmptyLeftBehindAnimationProvider implements LeftBehindViewAnimationProvider {
    private static EmptyLeftBehindAnimationProvider sInstance;
    private EmptyLeftBehindViewAnimation mAnimation;

    private EmptyLeftBehindAnimationProvider() {
        mAnimation = new EmptyLeftBehindViewAnimation();
    }

    public static EmptyLeftBehindAnimationProvider get() {
        if (sInstance == null) {
            sInstance = new EmptyLeftBehindAnimationProvider();
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
