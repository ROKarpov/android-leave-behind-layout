package com.romankarpov.leavebehindlayout.core;

import android.support.animation.DynamicAnimation;
import android.view.VelocityTracker;

public abstract class AbstractVerticalInteractionModel extends AbstractInteractionModel {
    @Override
    public float getOpenedPosition() {
        return mLeftBehindViewBehavior.getVerticalOpenOffset();
    }

    @Override
    public float getFlewOutPosition() {
        return mForeView.getHeight();
    }

    @Override
    public float selectValue(float x, float y) {
        return y;
    }

    @Override
    public DynamicAnimation.ViewProperty getAnimatedProperty() {
        return DynamicAnimation.TRANSLATION_Y;
    }

    @Override
    protected void setCurrentPosition(float offset) {
        mForeView.setTranslationY(offset);
    }

    @Override
    public float getCurrentPosition() {
        return mForeView.getTranslationY();
    }

    @Override
    public float getVelocityFrom(VelocityTracker tracker) {
        return tracker.getYVelocity();
    }
}
