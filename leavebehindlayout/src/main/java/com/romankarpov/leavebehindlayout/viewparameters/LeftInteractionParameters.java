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
    public float calculateOpeningProgress() {
        return ViewCompat.getTranslationX(getForeView()) / getLeftBehindView().getMeasuredWidth();
    }
    @Override
    public float calculateFlyingOutProgress() {
        final View foreView =  getForeView();
        return ViewCompat.getTranslationX(foreView) / foreView.getMeasuredWidth();
    }

    @Override
    public boolean isInteractionStarted(float dx, float dy, float absDx, float absDy, float touchSlop) {
        return (absDx >= absDy) && (absDx > touchSlop) && (dx > 0);
    }
    @Override
    public boolean isOffsetApplicable(float offset) {
        return offset >= 0;
    }

    @Override
    public boolean shouldOpen(float velocityX, float velocityY, float progressThreshold) {
        final float openProgress = calculateOpeningProgress();
        return isOpenable()
                && (velocityX >= 0.f)
                && (openProgress >= progressThreshold)
                && ((openProgress <= 1.f) || !isFlyoutable() || velocityX == 0.f);
    }
    @Override
    public boolean shouldFlyout(float velocityX, float velocityY, float progressThreshold) {
        return isFlyoutable()
                && (velocityX >= 0.f)
                && ((isOpenable() && calculateOpeningProgress() > 1.f )
                    || (!isOpenable() && calculateFlyingOutProgress() >= progressThreshold)
                );
    }

    @Override
    public float getClosedOffset() {
        return 0;
    }
    @Override
    public float getOpenedOffset() {
        return getLeftBehindView().getWidth();
    }
    @Override
    public float getFlewOutOffset() {
        return getForeView().getWidth();
    }
    @Override
    public float getCurrentOffset() {
        return getForeView().getTranslationX();
    }
    @Override
    public float getVelocityFrom(VelocityTracker tracker) {
        return tracker.getXVelocity();
    }
    @Override
    public DynamicAnimation.ViewProperty getAnimatedProperty() {
        return DynamicAnimation.TRANSLATION_X;
    }
    @Override
    public float selectValue(float x, float y) {
        return x;
    }
}
