package com.romankarpov.leavebehindlayout;

import android.view.View;

import org.jetbrains.annotations.NotNull;

class OpenableViewBehavior implements LeftBehindViewBehavior {
    InteractionModel mOwner;
    View mLeftBehindView;
    LeftBehindViewAnimator mAnimator;
    ApplyOffsetCallback mApplyOffsetCallback;

    public OpenableViewBehavior (
            @NotNull InteractionModel owner,
            @NotNull View leftBehindView,
            @NotNull LeftBehindViewAnimator animator,
            @NotNull ApplyOffsetCallback callback) {
        mOwner = owner;
        mLeftBehindView = leftBehindView;
        mAnimator = animator;
        mApplyOffsetCallback = callback;
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
        mApplyOffsetCallback.onApplyOffset(
                offset, mOwner.getOpeningProgress(),mOwner.getGravity(),mLeftBehindView);
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

    interface ApplyOffsetCallback {
        void onApplyOffset(float offset, float progress, int gravity, View view);
    }
}
