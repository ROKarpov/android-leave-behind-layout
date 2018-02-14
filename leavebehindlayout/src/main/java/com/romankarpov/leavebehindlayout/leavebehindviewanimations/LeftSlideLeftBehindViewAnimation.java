package com.romankarpov.leavebehindlayout.leavebehindviewanimations;

import android.support.v4.view.ViewCompat;
import android.view.View;

public class LeftSlideLeftBehindViewAnimation implements LeftBehindViewAnimation {
    @Override
    public void apply(View view, float value) {
        final float width = view.getWidth();
        final float normalizedValue = Math.min(width, value);
        view.setTranslationX(normalizedValue - width);
    }
}
