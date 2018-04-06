package com.romankarpov.leavebehindlayout.core;

import android.support.animation.DynamicAnimation;
import android.view.VelocityTracker;

public abstract class AbstractHorizontalInteractionModel extends AbstractInteractionModel {
    @Override
    public float selectValue(float x, float y) {
        return x;
    }

    @Override
    public DynamicAnimation.ViewProperty getAnimatedProperty() {
        return DynamicAnimation.TRANSLATION_X;
    }

    @Override
    protected void setCurrentPosition(float offset) {
        mForeView.setTranslationX(offset);
    }

    @Override
    public float getCurrentPosition() {
        return mForeView.getTranslationX();
    }

    @Override
    public float getVelocityFrom(VelocityTracker tracker) {
        return tracker.getXVelocity();
    }
}
