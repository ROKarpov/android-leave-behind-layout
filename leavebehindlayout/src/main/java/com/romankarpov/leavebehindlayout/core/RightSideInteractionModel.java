package com.romankarpov.leavebehindlayout.core;

import android.view.View;

import com.romankarpov.leavebehindlayout.LeaveBehindLayout;


public class RightSideInteractionModel extends AbstractHorizontalInteractionModel {
    @Override
    public float getOpenedPosition() {
        return -mLeftBehindViewBehavior.getHorizontalOpenOffset();
    }
    @Override
    public float getFlewOutPosition() {
        return -mForeView.getWidth();
    }

    @Override
    public boolean isInteractionStarted(float dx, float dy, float absDx, float absDy) {
        return (absDx > absDy) && (dx < 0);
    }

    @Override
    public boolean isApplicable(float offset) {
        return offset <= 0;
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l,t,r,b);

        View view = getLeftBehindView();
        if (view == null) return;

        LeaveBehindLayout.LayoutParams lp = (LeaveBehindLayout.LayoutParams)view.getLayoutParams();

        final int right = r - lp.rightMargin;
        final int top = t + lp.topMargin;
        final int left = right - view.getMeasuredWidth();
        final int bottom = top + view.getMeasuredHeight();
        view.layout(left, top, right, bottom);
    }

    @Override
    public void applyAnimation(View view, float value) {
        mLeftBehindViewAnimation.applyRight(view, value);
    }
}
