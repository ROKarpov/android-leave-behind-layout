package com.romankarpov.leavebehindlayout.viewparameters;

import android.support.animation.DynamicAnimation;
import android.view.VelocityTracker;
import android.view.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface InteractionParameters {
    void setForeView(@NotNull View view);
    @NotNull View getForeView();

    void setLeftBehindView(@Nullable View view);
    @Nullable View getLeftBehindView();
    int getLeftBehindGravity();

    void hideLeftBehindView();
    void showLeftBehindView();

    // To prevent multiple abs values calculations, they are also passed.
    boolean isInteractionStarted(float dx, float dy, float absDx, float absDy, float touchSlop);
    boolean areOffsetsApplicable(float offsetX, float offsetY);

    boolean shouldOpen(float velocityX, float velocityY, float progressThreshold);
    boolean shouldFlyout(float velocityX, float velocityY, float progressThreshold);

    void applyOffset(float offsetX, float offsetY);
    float calculateOpeningProgress();
    float calculateFlyingOutProgress();

    float getCurrentPositionX();
    float getCurrentPositionY();

    void applyOffset(float offset);
    float getClosedOffset();
    float getOpenedOffset();
    float getFlewOutOffset();
    float getVelocityFrom(VelocityTracker tracker);
    DynamicAnimation.ViewProperty getAnimatedProperty();
    void applyLeftBehindViewAnimation(float value);
}
