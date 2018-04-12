package com.romankarpov.leavebehindlayout;

import android.view.View;

class EmptyViewBehavior implements LeftBehindViewBehavior {
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
