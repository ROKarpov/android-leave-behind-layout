package com.romankarpov.leavebehindlayout;

import android.support.animation.DynamicAnimation;
import android.view.MotionEvent;

public interface LeaveBehindLayoutState {
    void applyLayout(LeaveBehindLayout layout);

    boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event);
    boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event);

    float getFinalPositionFrom(InteractionModel interactionModel);
    DynamicAnimation.OnAnimationUpdateListener getAnimationUpdateListener(LeaveBehindLayout layout);
    DynamicAnimation.OnAnimationEndListener getAnimationEndListener(LeaveBehindLayout layout);

    void onStateSpecified(LeaveBehindLayout layout);
}
