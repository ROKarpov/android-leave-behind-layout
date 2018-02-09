package com.romankarpov.leavebehindlayout.viewparameters;

import android.support.animation.DynamicAnimation;
import android.support.v4.view.ViewCompat;
import android.view.VelocityTracker;
import android.view.View;
import org.jetbrains.annotations.NotNull;
import com.romankarpov.leavebehindlayout.leavebehindviewanimations.LeftBehindViewAnimation;


public class BottomInteractionParameters extends AbstractInteractionParameters {
    public BottomInteractionParameters(
            @NotNull View leftBehindView,
            int gravity,
            @NotNull LeftBehindViewAnimation leftBehindViewAnimation,
            boolean isOpenable,
            boolean isFlyoutable
    ) {
        super(leftBehindView, gravity, leftBehindViewAnimation, isOpenable, isFlyoutable);
    }

    @Override
    public float calculateOpenProgress() {
        return -ViewCompat.getTranslationY(getForeView()) / getLeftBehindView().getMeasuredHeight();
    }
    @Override
    public float calculateFlyoutProgress() {
        final View foreView = getForeView();
        return -ViewCompat.getTranslationY(foreView) / foreView.getMeasuredHeight();
    }

    @Override
    public boolean isInteractionStarted(float dx, float dy, float absDx, float absDy, float touchSlop) {
        return (absDy > absDx) && (absDy >= touchSlop) && (dy < 0);
    }
    @Override
    public boolean areOffsetsApplicable(float offsetX, float offsetY) {
        return offsetY <= 0;
    }
    @Override
    public boolean shouldOpen(float velocityX, float velocityY, float progressThreshold) {
        final float openProgress = calculateOpenProgress();
        return (isOpenable()
                && (velocityY <= 0.f)
                && (openProgress >= progressThreshold)
                && ((openProgress <= 1.f) || !isFlyoutable() || velocityY == 0.f)
        );
    }
    @Override
    public boolean shouldFlyout(float velocityX, float velocityY, float progressThreshold) {
        return isFlyoutable()
                && (velocityY <= 0.f)
                && ((isOpenable() && (calculateOpenProgress() > 1.f) && (velocityY < 0.f))
                || (!isOpenable() && (calculateFlyoutProgress() >= progressThreshold))
        );
    }

    @Override
    public float clipOffsetX(float offsetX) {
        return 0;
    }
    @Override
    public float clipOffsetY(float offsetY) {
        return offsetY;
    }
    @Override
    public float clipVelocityX(float velocity, float minVelocity, float maxVelocity) {
        return 0;
    }
    @Override
    public float clipVelocityY(float velocity, float minVelocity, float maxVelocity) {
        return clipVelocity(velocity, minVelocity, maxVelocity);
    }

    @Override
    public float getOpenedPositionX() {
        return 0;
    }
    @Override
    public float getOpenedPositionY() {
        return -getLeftBehindView().getHeight();
    }
    @Override
    public float getFlyoutPositionX() {
        return 0;
    }
    @Override
    public float getFlyoutPositionY() {
        return -getForeView().getHeight();
    }


    @Override
    public float getClosedPosition() {
        return 0;
    }
    @Override
    public float getOpenedPosition() {
        return -getLeftBehindView().getHeight();
    }
    @Override
    public float getFlewOutPosition() {
        return -getForeView().getHeight();
    }
    @Override
    public float getVelocityFrom(VelocityTracker tracker) {
        return tracker.getYVelocity();
    }
    @Override
    public DynamicAnimation.ViewProperty getAnimatedProperty() {
        return DynamicAnimation.TRANSLATION_Y;
    }
}