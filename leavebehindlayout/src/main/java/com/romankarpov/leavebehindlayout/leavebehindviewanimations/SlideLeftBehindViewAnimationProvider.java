package com.romankarpov.leavebehindlayout.leavebehindviewanimations;

public final class SlideLeftBehindViewAnimationProvider implements LeftBehindViewAnimationProvider {
    private static SlideLeftBehindViewAnimationProvider sInstance;
    private LeftBehindViewAnimation mLeftAnimation;
    private LeftBehindViewAnimation mTopAnimation;
    private LeftBehindViewAnimation mRightAnimation;
    private LeftBehindViewAnimation mBottomAnimation;


    private SlideLeftBehindViewAnimationProvider() {}

    public static SlideLeftBehindViewAnimationProvider get() {
        if (sInstance == null) {
            sInstance = new SlideLeftBehindViewAnimationProvider();
        }
        return sInstance;
    }

    @Override
    public LeftBehindViewAnimation getLeftAnimation() {
        if (mLeftAnimation == null) {
            mLeftAnimation = new LeftSlideLeftBehindViewAnimation();
        }
        return mLeftAnimation;
    }

    @Override
    public LeftBehindViewAnimation getTopAnimation() {
        if (mTopAnimation == null) {
            mTopAnimation = new TopSlideLeftBehindViewAnimation();
        }
        return mTopAnimation;
    }

    @Override
    public LeftBehindViewAnimation getRightAnimation() {
        if (mRightAnimation == null) {
            mRightAnimation = new RightSlideLeftBehindViewAnimation();
        }
        return mRightAnimation;
    }

    @Override
    public LeftBehindViewAnimation getBottomAnimation() {
        if (mBottomAnimation == null) {
            mBottomAnimation = new BottomSlideLeftBehindViewAnimation();
        }
        return mBottomAnimation;
    }
}
