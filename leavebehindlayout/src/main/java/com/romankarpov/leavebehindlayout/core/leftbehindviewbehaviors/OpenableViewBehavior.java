package com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors;

import android.view.View;

import com.romankarpov.leavebehindlayout.animations.LeftBehindViewAnimator;

public class OpenableViewBehavior implements LeftBehindViewBehavior {
    View mLeftBehindView;
    LeftBehindViewAnimator mAnimator;

    public OpenableViewBehavior (View leftBehindView, LeftBehindViewAnimator animator) {
        mLeftBehindView = leftBehindView;
        mAnimator = animator;
    }

    @Override
    public boolean isAvailable() {
        return true;
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
        // TODO: REMOVE CHECKING!
        if (mAnimator == null) return;
        mAnimator.applyAnimation(mLeftBehindView, offset);
    }

    @Override
    public float getHorizontalOpenOffset() {
        return mLeftBehindView.getWidth();
    }

    @Override
    public float getVerticalOpenOffset() {
        return mLeftBehindView.getHeight();
    }
}
