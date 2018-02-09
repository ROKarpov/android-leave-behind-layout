package com.romankarpov.leavebehindlayout.leavebehindviewanimations;

public final class SlideLeftBehindAnimationProvider implements LeftBehindViewAnimationProvider {
    private static SlideLeftBehindAnimationProvider sInstance;

    private SlideLeftBehindAnimationProvider() {}

    public static SlideLeftBehindAnimationProvider get() {
        if (sInstance == null) {
            sInstance = new SlideLeftBehindAnimationProvider();
        }
        return sInstance;
    }

    @Override
    public LeftBehindViewAnimation getLeftAnimation() {
        return null;
    }

    @Override
    public LeftBehindViewAnimation getTopAnimation() {
        return null;
    }

    @Override
    public LeftBehindViewAnimation getRightAnimation() {
        return null;
    }

    @Override
    public LeftBehindViewAnimation getBottomAnimation() {
        return null;
    }
}
