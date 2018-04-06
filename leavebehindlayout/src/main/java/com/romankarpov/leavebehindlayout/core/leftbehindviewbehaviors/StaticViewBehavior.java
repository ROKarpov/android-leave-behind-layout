package com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors;

import android.view.View;

public class StaticViewBehavior implements LeftBehindViewBehavior {
    View mLeftBehindView;

    public StaticViewBehavior(View leftBehindView) {
        mLeftBehindView = leftBehindView;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isInteractWith(View view) {
        return view == mLeftBehindView;
    }

    @Override
    public View getView() {
        return mLeftBehindView;
    }

    @Override
    public void applyOffset(float offset) {

    }

    @Override
    public float getHorizontalOpenOffset() {
        return 0;
    }

    @Override
    public float getVerticalOpenOffset() {
        return 0;
    }
}
