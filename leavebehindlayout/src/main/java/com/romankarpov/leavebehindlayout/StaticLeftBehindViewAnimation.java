package com.romankarpov.leavebehindlayout;

import android.view.View;

public class StaticLeftBehindViewAnimation implements LeftBehindViewAnimation {
    private static StaticLeftBehindViewAnimation sInstance;

    @Override
    public void applyLeft(View view, float value) {
    }
    @Override
    public void applyTop(View view, float value) {
    }
    @Override
    public void applyRight(View view, float value) {
    }
    @Override
    public void applyBottom(View view, float value) {
    }

    public static StaticLeftBehindViewAnimation get() {
        if (sInstance == null) {
            sInstance = new StaticLeftBehindViewAnimation();
        }
        return sInstance;
    }
}
