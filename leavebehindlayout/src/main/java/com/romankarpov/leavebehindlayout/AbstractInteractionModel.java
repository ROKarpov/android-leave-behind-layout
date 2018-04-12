package com.romankarpov.leavebehindlayout;

import android.support.animation.DynamicAnimation;
import android.view.VelocityTracker;
import android.view.View;

import org.jetbrains.annotations.NotNull;

abstract class AbstractInteractionModel implements InteractionModel, LeftBehindViewAnimator {
    private int mGravity;
    private UnavailableOffsetBehavior mUnavailableOffsetBehavior;
    View mForeView;
    LeftBehindViewBehavior mLeftBehindViewBehavior;
    FlyoutBehavior mFlyingOutBehavior;
    LeftBehindViewAnimation mLeftBehindViewAnimation;

    AbstractInteractionModel setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }
    AbstractInteractionModel setUnavailableOffsetBehavior(@NotNull UnavailableOffsetBehavior unavailableOffsetBehavior) {
        mUnavailableOffsetBehavior = unavailableOffsetBehavior;
        return this;
    }
    AbstractInteractionModel setForeView(View foreView) {
        mForeView = foreView;
        return this;
    }
    AbstractInteractionModel setLeftBehindViewBehavior(@NotNull LeftBehindViewBehavior leftBehindViewBehavior) {
        mLeftBehindViewBehavior = leftBehindViewBehavior;
        return this;
    }
    AbstractInteractionModel setFlyingOutBehavior(@NotNull FlyoutBehavior flyingOutBehavior) {
        mFlyingOutBehavior = flyingOutBehavior;
        return this;
    }
    AbstractInteractionModel setAnimation(@NotNull LeftBehindViewAnimation animation) {
        mLeftBehindViewAnimation = animation;
        return this;
    }

    public void appendOffset(float offset) {
        applyOffset(getCurrentPosition() + offset);
    }
    public void applyOffset(float offset) {
        if (isApplicable(offset)) {
            setCurrentPosition(offset);
            mLeftBehindViewBehavior.applyOffset(offset);
            //mFlyingOutBehavior.applyOffset(offset);
        } else {
            mUnavailableOffsetBehavior.onUnavailableOffset(this, offset);
        }
    }

    public int getGravity() {
        return mGravity;
    }

    public float getClosedPosition() {
        return 0;
    }
    public abstract float getCurrentPosition();
    public abstract float getOpenedPosition();
    public abstract float getFlewOutPosition();
    public float getOpeningProgress() {
        return getCurrentPosition() / getOpenedPosition();
    }
    public float getFlyingOutProgress() {
        return getCurrentPosition() / getFlewOutPosition();
    }


    public void startInteraction() {
        mLeftBehindViewBehavior.onInteractionStart();
    }
    public void endInteraction() {
        mLeftBehindViewBehavior.onInteractionEnd();
    }

    public void animateLeftBehindView(float value) {
        mLeftBehindViewBehavior.applyOffset(value);
    }

    public boolean isInteractsWith(View view) {
        return mLeftBehindViewBehavior.isInteractWith(view);
    }
    public abstract boolean isApplicable(float value);
    public boolean isOpenable() {
        return mLeftBehindViewBehavior.isAvailable();
    }
    public boolean isFlyoutable() {
        return mFlyingOutBehavior.isAvailable();
    }

    public View getForeView() {
        return mForeView;
    }
    public View getLeftBehindView() {
        return mLeftBehindViewBehavior.getView();
    }

    public abstract float getVelocityFrom(VelocityTracker tracker);

    public abstract float selectValue(float x, float y);
    public abstract boolean isInteractionStarted(float dx, float dy, float absDx, float absDy);
    public abstract DynamicAnimation.ViewProperty getAnimatedProperty();
    public boolean isPointInForeView(float x, float y) {
        final float left = mForeView.getTranslationX();
        final float top = mForeView.getTranslationY();
        final float right = left + mForeView.getWidth();
        final float bottom = top + mForeView.getHeight();
        return ((left <= x) && (right >= x) && (top <= y) && (bottom >= y));
    }

    public void layout(int l, int t, int r, int b) {
        LeaveBehindLayout.LayoutParams lp = (LeaveBehindLayout.LayoutParams)mForeView.getLayoutParams();

        final int left = l + lp.leftMargin;
        final int top = t + lp.topMargin;
        final int right = left + mForeView.getMeasuredWidth();
        final int bottom = top + mForeView.getMeasuredHeight();
        mForeView.layout(left, top, right, bottom);
    }

    public abstract void applyAnimation(View view, float value);

    protected abstract void setCurrentPosition(float position);
}