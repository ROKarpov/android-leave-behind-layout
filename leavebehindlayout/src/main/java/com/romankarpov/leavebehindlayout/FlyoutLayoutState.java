package com.romankarpov.leavebehindlayout;

import android.support.animation.DynamicAnimation;
import android.view.MotionEvent;
import android.view.View;

class FlyoutLayoutState implements LeaveBehindLayoutState {
    @Override
    public int getFlag() {
        return LeaveBehindLayout.FLAG_FLYOUT;
    }

    @Override
    public void applyLayout(LeaveBehindLayout layout) {

    }

    @Override
    public boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        return false;
    }

    @Override
    public boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        return false;
    }

    @Override
    public float getFinalPositionFrom(LeaveBehindLayoutConfig config) {
        return config.getFlewOutPosition();
    }

    @Override
    public DynamicAnimation.OnAnimationUpdateListener getAnimationUpdateListener(LeaveBehindLayout layout) {
        return new AnimationUpdateListener(layout);
    }

    @Override
    public DynamicAnimation.OnAnimationEndListener getAnimationEndListener(LeaveBehindLayout layout) {
        return new AnimationEndListener(layout);
    }

    class AnimationUpdateListener implements DynamicAnimation.OnAnimationUpdateListener {
        LeaveBehindLayout mLayout;

        public AnimationUpdateListener(LeaveBehindLayout layout) {
            mLayout = layout;
        }

        @Override
        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
            final LeaveBehindLayoutConfig config = mLayout.getConfig();
            final int gravity = config.getLeftBehindGravity();
            final float progress = config.calculateFlyingOutProgress();
            mLayout.dispatchFlyingOut(gravity, progress);
        }
    }

    class AnimationEndListener implements DynamicAnimation.OnAnimationEndListener {
        private LeaveBehindLayout mLayout;

        public AnimationEndListener(LeaveBehindLayout layout) {
            mLayout = layout;
        }
        @Override
        public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
            final LeaveBehindLayoutConfig config = mLayout.getConfig();
            mLayout.dispatchFlewOut(config.getLeftBehindGravity());
        }
    }}
