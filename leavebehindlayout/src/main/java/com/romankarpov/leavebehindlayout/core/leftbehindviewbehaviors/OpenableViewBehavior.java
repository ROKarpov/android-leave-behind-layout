package com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors;

import android.view.View;

import com.romankarpov.leavebehindlayout.core.LeftBehindViewAnimator;

import org.jetbrains.annotations.NotNull;

public class OpenableViewBehavior implements LeftBehindViewBehavior {
    View mLeftBehindView;
    LeftBehindViewAnimator mAnimator;

    public OpenableViewBehavior (@NotNull View leftBehindView, @NotNull LeftBehindViewAnimator animator) {
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

    @Override
    public void onInteractionStart() {
        mLeftBehindView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInteractionEnd() {
        mLeftBehindView.setVisibility(View.INVISIBLE);
    }
}
