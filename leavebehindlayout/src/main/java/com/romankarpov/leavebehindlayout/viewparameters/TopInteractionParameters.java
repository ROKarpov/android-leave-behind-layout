package com.romankarpov.leavebehindlayout.viewparameters;

import android.support.animation.DynamicAnimation;
import android.support.v4.view.ViewCompat;
import android.view.VelocityTracker;
import android.view.View;
import org.jetbrains.annotations.NotNull;
import com.romankarpov.leavebehindlayout.leavebehindviewanimations.LeftBehindViewAnimation;


public class TopInteractionParameters extends AbstractInteractionParameters {
    public TopInteractionParameters(
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
        return ViewCompat.getTranslationY(getForeView()) / getLeftBehindView().getMeasuredHeight();
    }
    @Override
    public float calculateFlyingOutProgress() {
        View foreView = getForeView();
        return ViewCompat.getTranslationY(foreView) / foreView.getMeasuredHeight();
    }

    @Override
    public boolean isInteractionStarted(float dx, float dy, float absDx, float absDy, float touchSlop) {
        return (absDy > absDx) && (absDy >= touchSlop) && (dy > 0);
    }
    @Override
    public boolean isOffsetApplicable(float offset) {
        return offset >= 0;
    }

    @Override
    public boolean shouldOpen(float velocityX, float velocityY, float progressThreshold) {
        final float openProgress = calculateOpeningProgress();
        return (isOpenable()
                && (velocityY >= 0.f)
                && (openProgress >= progressThreshold)
                && ((openProgress <= 1.f) || !isFlyoutable() || velocityY == 0.f)
        );
    }
    @Override
    public boolean shouldFlyout(float velocityX, float velocityY, float progressThreshold) {
        return isFlyoutable()
                && (velocityY >= 0.f)
                && ((isOpenable() && (calculateOpeningProgress() > 1.f) && (velocityY > 0.f))
                || (!isOpenable() && (calculateFlyingOutProgress() >= progressThreshold))
        );
    }

    @Override
    public float getClosedOffset() {
        return 0;
    }
    @Override
    public float getOpenedOffset() {
        return getLeftBehindView().getHeight();
    }
    @Override
    public float getCurrentOffset() {
        return getForeView().getTranslationY();
    }
    @Override
    public float getFlewOutOffset() {
        return getForeView().getHeight();
    }
    @Override
    public float getVelocityFrom(VelocityTracker tracker) {
        return tracker.getYVelocity();
    }
    @Override
    public DynamicAnimation.ViewProperty getAnimatedProperty() {
        return DynamicAnimation.TRANSLATION_Y;
    }
    @Override
    public float selectValue(float x, float y) {
        return y;
    }

}