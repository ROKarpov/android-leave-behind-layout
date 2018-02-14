package com.romankarpov.leavebehindlayout.leavebehindviewanimations;

import android.support.v4.view.ViewCompat;
import android.view.View;

public class BottomSlideLeftBehindViewAnimation implements LeftBehindViewAnimation {
    @Override
    public void apply(View view, float value) {
        final float height = view.getHeight();
        final float normalizedValue = Math.max(-height, value);
        view.setTranslationY(height + normalizedValue);
    }
}
