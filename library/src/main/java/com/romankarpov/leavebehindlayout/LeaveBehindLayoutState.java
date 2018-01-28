package com.romankarpov.leavebehindlayout;


import android.view.MotionEvent;

import com.romankarpov.leavebehindlayout.physics.PhysicsAnimationConfig;

interface LeaveBehindLayoutState {
    void applyLayout(LeaveBehindLayout layout);

    int getFlag();

    boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event);
    boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event);

    PhysicsAnimationConfig createAnimationConfig(LeaveBehindLayout layout);
}
