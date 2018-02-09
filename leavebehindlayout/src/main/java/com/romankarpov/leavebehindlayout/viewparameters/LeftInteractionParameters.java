package com.romankarpov.leavebehindlayout.viewparameters;

import android.support.animation.DynamicAnimation;
import android.support.v4.view.ViewCompat;
import android.view.VelocityTracker;
import android.view.View;
import org.jetbrains.annotations.NotNull;
import com.romankarpov.leavebehindlayout.leavebehindviewanimations.LeftBehindViewAnimation;


public class LeftInteractionParameters extends AbstractInteractionParameters {
    public LeftInteractionParameters(
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
        return ViewCompat.getTranslationX(getForeView()) / getLeftBehindView().getMeasuredWidth();
    }
    @Override
    public float calculateFlyoutProgress() {
        final View foreView =  getForeView();
        return ViewCompat.getTranslationX(foreView) / foreView.getMeasuredWidth();
    }

    @Override
    public float getOpenedPositionX() {
        return getLeftBehindView().getWidth();
    }
    @Override
    public float getOpenedPositionY() {
        return 0;
    }
    @Override
    public float getFlyoutPositionX() {
        return getForeView().getWidth();
    }
    @Override
    public float getFlyoutPositionY() {
        return 0;
    }

    @Override
    public float clipOffsetX(float offset) {
        return offset;
    }
    @Override
    public float clipOffsetY(float offset) {
        return 0;
    }
    @Override
    public float clipVelocityX(float velocity, float minVelocity, float maxVelocity) {
        return clipVelocity(velocity, minVelocity, maxVelocity);
    }
    @Override
    public float clipVelocityY(float velocity, float minVelocity, float maxVelocity) {
        return 0;
    }

    @Override
    public boolean isInteractionStarted(float dx, float dy, float absDx, float absDy, float touchSlop) {
        return (absDx >= absDy) && (absDx > touchSlop) && (dx > 0);
    }
    @Override
    public boolean areOffsetsApplicable(float offsetX, float offsetY) {
        return offsetX >= 0;
    }
    @Override
    public boolean shouldOpen(float velocityX, float velocityY, float progressThreshold) {
        final float openProgress = calculateOpenProgress();
        return isOpenable()
                && (velocityX >= 0.f)
                && (openProgress >= progressThreshold)
                && ((openProgress <= 1.f) || !isFlyoutable() || velocityX == 0.f);
    }
    @Override
    public boolean shouldFlyout(float velocityX, float velocityY, float progressThreshold) {
        return isFlyoutable()
                && (velocityX >= 0.f)
                && ((isOpenable() && calculateOpenProgress() > 1.f )
                    || (!isOpenable() && calculateFlyoutProgress() >= progressThreshold)
                );
    }

    @Override
    public float getClosedPosition() {
        return 0;
    }
    @Override
    public float getOpenedPosition() {
        return getLeftBehindView().getWidth();
    }
    @Override
    public float getFlewOutPosition() {
        return getForeView().getWidth();
    }
    @Override
    public float getVelocityFrom(VelocityTracker tracker) {
        return tracker.getXVelocity();
    }
    @Override
    public DynamicAnimation.ViewProperty getAnimatedProperty() {
        return DynamicAnimation.TRANSLATION_X;
    }
}
