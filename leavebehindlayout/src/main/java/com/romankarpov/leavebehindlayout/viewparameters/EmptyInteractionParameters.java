package com.romankarpov.leavebehindlayout.viewparameters;

import android.support.animation.DynamicAnimation;
import android.view.Gravity;
import android.view.VelocityTracker;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class EmptyInteractionParameters implements InteractionParameters {
    View mForeView;

    public EmptyInteractionParameters() { }

    @Override
    public View getForeView() {
        return mForeView;
    }
    @Override
    public void setForeView(@NotNull View view) {
        mForeView = view;
    }

    @Override
    @Nullable
    public View getLeftBehindView() {
        return null;
    }
    @Override
    public void setLeftBehindView(@Nullable View view) {

    }

    @Override
    public int getLeftBehindGravity() {
        return Gravity.NO_GRAVITY;
    }

    @Override
    public void hideLeftBehindView() {

    }
    @Override
    public void showLeftBehindView() {

    }

    @Override
    public boolean isInteractionStarted(float dx, float dy, float absDx, float absDy, float touchSlop) {
        return false;
    }

    @Override
    public boolean isOffsetApplicable(float offset) {
        return false;
    }

    @Override
    public float selectValue(float x, float y) {
        return 0;
    }

    @Override
    public boolean shouldOpen(float velocityX, float velocityY, float progressThreshold) {
        return false;
    }
    @Override
    public boolean shouldFlyout(float velocityX, float velocityY, float progressThreshold) {
        return false;
    }

    @Override
    public float calculateOpeningProgress() {
        return 0.f;
    }
    @Override
    public float calculateFlyingOutProgress() {
        return 0.f;
    }

    @Override
    public float getClosedOffset() {
        return 0;
    }
    @Override
    public float getOpenedOffset() {
        return 0;
    }
    @Override
    public float getFlewOutOffset() {
        return 0;
    }
    @Override
    public float getCurrentOffset() {
        return 0;
    }
    @Override
    public float getVelocityFrom(VelocityTracker tracker) {
        return 0;
    }

    @Override
    public DynamicAnimation.ViewProperty getAnimatedProperty() {
        return DynamicAnimation.TRANSLATION_X;
    }

    @Override
    public void applyLeftBehindViewAnimation(float value) {

    }

    @Override
    public void applyOffset(float offset) {

    }
}
