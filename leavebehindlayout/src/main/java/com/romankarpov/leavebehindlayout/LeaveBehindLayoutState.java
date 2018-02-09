package com.romankarpov.leavebehindlayout;

import android.view.MotionEvent;

interface LeaveBehindLayoutState {
    void applyLayout(LeaveBehindLayout layout);

    int getFlag();

    boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event);
    boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event);

    float getFinalPositionFrom(LeaveBehindLayoutConfig config);
}
