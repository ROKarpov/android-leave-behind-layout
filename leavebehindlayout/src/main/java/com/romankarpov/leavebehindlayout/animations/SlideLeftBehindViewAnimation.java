package com.romankarpov.leavebehindlayout.animations;

import android.view.View;

public class SlideLeftBehindViewAnimation implements LeftBehindViewAnimation {
    private static SlideLeftBehindViewAnimation sInstance;

    @Override
    public void applyLeft(View view, float value) {
        final float width = view.getWidth();
        final float normalizedValue = Math.min(width, value);
        view.setTranslationX(normalizedValue - width);
    }

    @Override
    public void applyTop(View view, float value) {
        final float height = view.getHeight();
        final float normalizedValue = Math.min(height, value);
        view.setTranslationX(normalizedValue - height);
    }

    @Override
    public void applyRight(View view, float value) {
        final float width = view.getWidth();
        final float normalizedValue = Math.max(-width, value);
        view.setTranslationX(width + normalizedValue);
    }

    @Override
    public void applyBottom(View view, float value) {
        final float height = view.getHeight();
        final float normalizedValue = Math.max(-height, value);
        view.setTranslationY(height + normalizedValue);
    }

    public static SlideLeftBehindViewAnimation get() {
        if (sInstance == null) {
            sInstance = new SlideLeftBehindViewAnimation();
        }
        return sInstance;
    }
}
