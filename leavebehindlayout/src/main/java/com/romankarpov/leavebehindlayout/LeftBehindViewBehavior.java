package com.romankarpov.leavebehindlayout;

import android.view.View;

interface LeftBehindViewBehavior {
    boolean isAvailable();
    boolean isInteractWith(View view);
    View getView();

    void onInteractionStart();
    void onInteractionEnd();

    // Not very clear API.
    void applyOffset(float offset);
    float getHorizontalOpenOffset();
    float getVerticalOpenOffset();
};