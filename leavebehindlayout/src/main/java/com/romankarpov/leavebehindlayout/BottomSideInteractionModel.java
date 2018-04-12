package com.romankarpov.leavebehindlayout;

import android.view.View;


class BottomSideInteractionModel extends AbstractVerticalInteractionModel {
    @Override
    public float getOpenedPosition() {
        return -mLeftBehindViewBehavior.getVerticalOpenOffset();
    }
    @Override
    public float getFlewOutPosition() {
        return -mForeView.getHeight();
    }

    @Override
    public boolean isInteractionStarted(float dx, float dy, float absDx, float absDy) {
        return (absDy > absDx) && (dy > 0);
    }

    @Override
    public boolean isApplicable(float value) {
        return value >= 0;
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l,t,r,b);

        View view = getLeftBehindView();
        if (view == null) return;

        LeaveBehindLayout.LayoutParams lp = (LeaveBehindLayout.LayoutParams)view.getLayoutParams();

        final int left = l + lp.leftMargin;
        final int bottom = b - lp.bottomMargin;
        final int right = left + view.getMeasuredWidth();
        final int top = bottom - view.getMeasuredHeight();
        view.layout(left, top, right, bottom);
    }

    @Override
    public void applyAnimation(View view, float value) {
        mLeftBehindViewAnimation.applyBottom(view, value);
    }
}
