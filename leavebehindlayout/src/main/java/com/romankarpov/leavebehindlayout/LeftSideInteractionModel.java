package com.romankarpov.leavebehindlayout;

import android.view.View;


class LeftSideInteractionModel extends AbstractHorizontalInteractionModel {
    @Override
    public float getOpenedPosition() {
        return mLeftBehindViewBehavior.getHorizontalOpenOffset();
    }
    @Override
    public float getFlewOutPosition() {
        return mForeView.getWidth();
    }

    @Override
    public float getOpeningProgress() {
        return getCurrentPosition() / getOpenedPosition();
    }
    @Override
    public float getFlyingOutProgress() {
        return getCurrentPosition() / getFlewOutPosition();
    }

    @Override
    public boolean isInteractionStarted(float dx, float dy, float absDx, float absDy) {
        return (absDx > absDy) && (dx > 0);
    }

    @Override
    public boolean isApplicable(float offset) {
        return offset >= 0;
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l,t,r,b);

        View view = getLeftBehindView();
        if (view == null) return;

        LeaveBehindLayout.LayoutParams lp = (LeaveBehindLayout.LayoutParams)view.getLayoutParams();

        final int left = l + lp.leftMargin;
        final int top = t + lp.topMargin;
        final int right = left + view.getMeasuredWidth();
        final int bottom = top + view.getMeasuredHeight();
        view.layout(left, top, right, bottom);
    }

    @Override
    public void applyAnimation(View view, float value) {
        mLeftBehindViewAnimation.applyLeft(view, value);
    }
}
