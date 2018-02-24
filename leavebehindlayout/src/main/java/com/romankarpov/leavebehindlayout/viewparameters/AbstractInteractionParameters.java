package com.romankarpov.leavebehindlayout.viewparameters;

import org.jetbrains.annotations.NotNull;

import android.support.animation.DynamicAnimation;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.VelocityTracker;
import android.view.View;


import com.romankarpov.leavebehindlayout.leavebehindviewanimations.LeftBehindViewAnimation;

public abstract class AbstractInteractionParameters implements InteractionParameters {
    private View mForeView;
    private View mLeftBehindView;
    private LeftBehindViewAnimation mLeftBehindViewAnimation;

    private int mGravity = Gravity.NO_GRAVITY;
    private boolean mIsOpenable = true;
    private boolean mIsFlyoutable = true;

    public AbstractInteractionParameters(
            @NotNull View leftBehindView,
            int gravity,
            @NotNull LeftBehindViewAnimation leftBehindViewAnimation,
            boolean isOpenable,
            boolean isFlyoutable) {
        mLeftBehindView = leftBehindView;
        mGravity = gravity;
        mLeftBehindViewAnimation = leftBehindViewAnimation;
        mIsOpenable = isOpenable;
        mIsFlyoutable = isFlyoutable;
    }

    @NotNull public View getForeView() { return mForeView; }
    public void setForeView(@NotNull View view) {
        mForeView = view;
    }

    @NotNull public View getLeftBehindView() {
        return mLeftBehindView;
    }
    public void setLeftBehindView(@NotNull View leftBehindView) {
        mLeftBehindView = leftBehindView;
    }

    @Override
    public int getLeftBehindGravity() {
        return mGravity;
    }

    public void hideLeftBehindView() {
        mLeftBehindView.setVisibility(View.INVISIBLE);
    }
    public void showLeftBehindView() {
        mLeftBehindView.setVisibility(View.VISIBLE);
    }

    @Override
    public void applyOffset(float offset) {
        getAnimatedProperty().setValue(this.getForeView(), offset);
        if (mIsOpenable) {
            mLeftBehindViewAnimation.apply(mLeftBehindView, offset);
        }
    }

    @Override
    public void applyLeftBehindViewAnimation(float value) {
        mLeftBehindViewAnimation.apply(mLeftBehindView, value);
    }

    public abstract float calculateOpeningProgress();
    public abstract float calculateFlyingOutProgress();

    // To prevent multiple abs values calculations, they are also passed.
    public abstract boolean isInteractionStarted(float dx, float dy, float absDx, float absDy, float touchSlop);
    public abstract boolean shouldOpen(float velocityX, float velocityY, float progressThreshold);
    public abstract boolean shouldFlyout(float velocityX, float velocityY, float progressThreshold);

    protected boolean isOpenable() {
        return mIsOpenable;
    }
    protected boolean isFlyoutable() {
        return mIsFlyoutable;
    }

    public abstract float getClosedOffset();
    public abstract float getOpenedOffset();
    public abstract float getFlewOutOffset();
    public abstract float getVelocityFrom(VelocityTracker tracker);
    public abstract DynamicAnimation.ViewProperty getAnimatedProperty();
}