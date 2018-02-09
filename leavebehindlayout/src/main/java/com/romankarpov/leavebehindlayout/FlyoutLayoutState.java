package com.romankarpov.leavebehindlayout;

import android.view.MotionEvent;

class FlyoutLayoutState implements LeaveBehindLayoutState {
    @Override
    public int getFlag() {
        return LeaveBehindLayout.FLAG_FLYOUT;
    }

    @Override
    public void applyLayout(LeaveBehindLayout layout) {

    }

    @Override
    public boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        return false;
    }

    @Override
    public boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        return false;
    }

    @Override
    public float getFinalPositionFrom(LeaveBehindLayoutConfig config) {
        return config.getFlewOutPosition();
    }
}
