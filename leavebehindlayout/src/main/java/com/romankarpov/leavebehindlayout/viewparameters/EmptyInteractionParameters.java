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
    public boolean areOffsetsApplicable(float offsetX, float offsetY) {
        return false;
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
    public void applyOffset(float offsetX, float offsetY) {

    }

    @Override
    public float getCurrentPositionX() {
        return 0;
    }
    @Override
    public float getCurrentPositionY() {
        return 0;
    }

    @Override
    public float getClosedPositionX() {
        return 0;
    }
    @Override
    public float getClosedPositionY() {
        return 0;
    }

    @Override
    public float getOpenedPositionX() {
        return 0.f;
    }
    @Override
    public float getOpenedPositionY() {
        return 0.f;
    }

    @Override
    public float getFlyoutPositionX() {
        return 0.f;
    }
    @Override
    public float getFlyoutPositionY() {
        return 0.f;
    }

    @Override
    public float clipVelocityX(float velocity, float minVelocity, float maxVelocity) {
        return 0;
    }
    @Override
    public float clipVelocityY(float velocity, float minVelocity, float maxVelocity) {
        return 0;
    }


    @Override
    public float getClosedPosition() {
        return 0;
    }

    @Override
    public float getOpenedPosition() {
        return 0;
    }

    @Override
    public float getFlewOutPosition() {
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
}
