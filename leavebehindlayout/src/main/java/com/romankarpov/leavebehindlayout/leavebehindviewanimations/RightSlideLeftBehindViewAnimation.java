package com.romankarpov.leavebehindlayout.leavebehindviewanimations;

import android.view.View;

public class RightSlideLeftBehindViewAnimation implements LeftBehindViewAnimation {
    @Override
    public void apply(View view, float value) {
        final float width = view.getWidth();
        final float normalizedValue = Math.max(-width, value);
        view.setTranslationX(width + normalizedValue);
    }
}
