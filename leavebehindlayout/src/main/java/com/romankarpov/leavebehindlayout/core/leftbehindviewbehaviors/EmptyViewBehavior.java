package com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors;

import android.view.View;

public class EmptyViewBehavior implements LeftBehindViewBehavior {
    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isInteractWith(View view) {
        return false;
    }

    @Override
    public View getView() {
        return null;
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

    @Override
    public void onInteractionStart() {

    }

    @Override
    public void onInteractionEnd() {

    }
}
