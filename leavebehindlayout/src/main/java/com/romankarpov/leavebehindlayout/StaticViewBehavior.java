package com.romankarpov.leavebehindlayout;

import android.view.View;

import org.jetbrains.annotations.NotNull;

class StaticViewBehavior implements LeftBehindViewBehavior {
    View mLeftBehindView;

    public StaticViewBehavior(@NotNull View leftBehindView) {
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

    @Override
    public void onInteractionStart() {
        mLeftBehindView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInteractionEnd() {
        mLeftBehindView.setVisibility(View.INVISIBLE);
    }
}
