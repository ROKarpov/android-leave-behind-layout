package com.romankarpov.leavebehindlayout.viewparameters;

import org.jetbrains.annotations.NotNull;

import android.support.v4.view.ViewCompat;
import android.view.Gravity;
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

    public abstract boolean areOffsetsApplicable(float offsetX, float offsetY);
    public void applyOffset(float offsetX, float offsetY) {
        final float offset = selectOffset(offsetX, offsetY);
        getAnimatedProperty().setValue(this.getForeView(), offset);
//        ViewCompat.setTranslationX(this.getForeView(), this.clipOffsetX(offsetX));
//        ViewCompat.setTranslationY(this.getForeView(), this.clipOffsetY(offsetY));

        final float progress = this.calculateOpeningProgress();
        mLeftBehindViewAnimation.apply(mLeftBehindView, offset);
    }

    public abstract float calculateOpeningProgress();
    public abstract float calculateFlyingOutProgress();

    protected abstract float clipOffsetX(float offset);
    protected abstract float clipOffsetY(float offset);
    public abstract float clipVelocityX(float velocity, float minVelocity, float maxVelocity);
    public abstract float clipVelocityY(float velocity, float minVelocity, float maxVelocity);

    public float getCurrentPositionX() {
        return mForeView.getTranslationX();
    }
    public float getCurrentPositionY() {
        return mForeView.getTranslationY();
    }

    public float getClosedPositionX() {
        return 0.f;
    }
    public float getClosedPositionY() {
        return 0.f;
    }

    public abstract float getOpenedPositionX();
    public abstract float getOpenedPositionY();

    public abstract float getFlyoutPositionX();
    public abstract float getFlyoutPositionY();


    // To prevent multiple abs values calculations, they are also passed.
    public abstract boolean isInteractionStarted(float dx, float dy, float absDx, float absDy, float touchSlop);
    public abstract boolean shouldOpen(float velocityX, float velocityY, float progressThreshold);
    public abstract boolean shouldFlyout(float velocityX, float velocityY, float progressThreshold);

    protected abstract float selectOffset(float offsetX, float offsetY);

    protected boolean isOpenable() {
        return mIsOpenable;
    }
    protected boolean isFlyoutable() {
        return mIsFlyoutable;
    }

    protected static float clipVelocity(float velocity, float minVelocity, float maxVelocity) {
        if ((velocity < minVelocity) && (velocity > -minVelocity)) {
            return 0;
        } else {
            return Math.max(-minVelocity, Math.min(maxVelocity, velocity));
        }
    }

    @Override
    public void applyLeftBehindViewAnimation(float value) {
        mLeftBehindViewAnimation.apply(mLeftBehindView, value);
    }
}