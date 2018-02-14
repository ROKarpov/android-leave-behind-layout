package com.romankarpov.leavebehindlayout.leavebehindviewanimations;

import android.support.v4.view.ViewCompat;
import android.view.View;

public class TopSlideLeftBehindViewAnimation implements LeftBehindViewAnimation {
    @Override
    public void apply(View view, float value) {
        final float height = view.getHeight();
        final float normalizedValue = Math.min(height, value);
        view.setTranslationX(normalizedValue - height);
    }
}
