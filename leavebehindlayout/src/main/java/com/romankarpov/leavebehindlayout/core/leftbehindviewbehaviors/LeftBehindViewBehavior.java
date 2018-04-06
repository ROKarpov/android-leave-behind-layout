package com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors;

import android.view.View;

public interface LeftBehindViewBehavior {
    boolean isAvailable();
    boolean isInteractWith(View view);
    View getView();

    // Not very clear API.
    void applyOffset(float offset);
    float getHorizontalOpenOffset();
    float getVerticalOpenOffset();
};