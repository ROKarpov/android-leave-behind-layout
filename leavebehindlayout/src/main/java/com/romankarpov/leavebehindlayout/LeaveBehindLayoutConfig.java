package com.romankarpov.leavebehindlayout;

import android.support.animation.DynamicAnimation;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.VelocityTracker;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import com.romankarpov.leavebehindlayout.leavebehindviewanimations.LeftBehindViewAnimationProvider;
import com.romankarpov.leavebehindlayout.viewparameters.BottomInteractionParameters;
import com.romankarpov.leavebehindlayout.viewparameters.EmptyInteractionParameters;
import com.romankarpov.leavebehindlayout.viewparameters.InteractionParameters;
import com.romankarpov.leavebehindlayout.viewparameters.LeftInteractionParameters;
import com.romankarpov.leavebehindlayout.viewparameters.RightInteractionParameters;
import com.romankarpov.leavebehindlayout.viewparameters.TopInteractionParameters;

final class LeaveBehindLayoutConfig {
    private static final String REPEATED_GRAVITY_MSG = "The layout already contains the view with the \'%s\' gravity ";
    private static final String INVALID_GRAVITY_MSG = "The layout does not support the specified gravity";
    private static final String NULL_FORE_VIEW_MSG = "The layout should contain a view with the \'none\' gravity.";

    private boolean mIsInitialized;

    private static final Integer EMPTY_OPENING_GRAVITY = Gravity.NO_GRAVITY;
    private static final InteractionParameters EMPTY_INTERACTION_PARAMETERS = new EmptyInteractionParameters();
    private static final float DEFAULT_OPEN_THRESHOLD = 0.5f;

    private Map<Integer, InteractionParameters> mAllOpeningParameters;
    private Integer mCurrentOpeningGravity;
    private InteractionParameters mCurrentOpeningParameters;


    private int mTouchSlop;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;
    private float mOpenThreshold;

    public LeaveBehindLayoutConfig(
            int touchSlop,
            int minFlingVelocity,
            int maxFlingVelocity
    ) {
        mIsInitialized = false;
        mTouchSlop = touchSlop;
        mMinFlingVelocity = minFlingVelocity;
        mMaxFlingVelocity = maxFlingVelocity;
        mOpenThreshold = DEFAULT_OPEN_THRESHOLD;

        mCurrentOpeningGravity = EMPTY_OPENING_GRAVITY;
        mCurrentOpeningParameters = EMPTY_INTERACTION_PARAMETERS;
        mAllOpeningParameters = new HashMap<>(3);
        mAllOpeningParameters.put(EMPTY_OPENING_GRAVITY, EMPTY_INTERACTION_PARAMETERS);
    }

    public @NotNull View getForeView() {
        return mCurrentOpeningParameters.getForeView();
    }
    public @Nullable View getLeftBehindView() {
        return mCurrentOpeningParameters. getLeftBehindView();
    }

    public int getLeftBehindGravity() { return mCurrentOpeningParameters.getLeftBehindGravity(); }

    public void applyOffset(float offset) {
        mCurrentOpeningParameters.applyOffset(offset);
    }
    public float getClosedPosition() {
        return mCurrentOpeningParameters.getClosedOffset();
    }
    public float getOpenedPosition() {
        return mCurrentOpeningParameters.getOpenedOffset();
    }
    public float getFlewOutPosition() {
        return mCurrentOpeningParameters.getFlewOutOffset();
    }
    public DynamicAnimation.ViewProperty getAnimatedProperty() {
        return mCurrentOpeningParameters.getAnimatedProperty();
    }
    public float getVelocityFrom(VelocityTracker tracker) {
        return mCurrentOpeningParameters.getVelocityFrom(tracker);
    }
    public float calculateOpeningProgress() {
        return mCurrentOpeningParameters.calculateOpeningProgress();
    }
    public float calculateFlyingOutProgress() {
        return mCurrentOpeningParameters.calculateFlyingOutProgress();
    }

    public boolean isPointInForeView(float x, float y) {
        View foreView = mCurrentOpeningParameters.getForeView();
        final float left = ViewCompat.getX(foreView);
        final float top = ViewCompat.getY(foreView);
        final float right = left + foreView.getWidth();
        final float bottom = top + foreView.getHeight();
        return ((left <= x) && (right >= x) && (top <= y) && (bottom >= y));
    }
    public boolean canSelectOpeningParameters(float dx, float dy) {
        return (dx > mTouchSlop) || (dx < -mTouchSlop) || (dy > mTouchSlop) || (dy < -mTouchSlop);
    }
    public boolean isTargetOffset(float dx, float dy) {
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);
        final boolean isOffsetHorizontal = absDx > absDy;
        if (isOffsetHorizontal) {
            return ((absDx > mTouchSlop)
                    && ((mCurrentOpeningGravity == Gravity.LEFT)
                        || (mCurrentOpeningGravity == Gravity.RIGHT))
            );
        } else {
            return ((absDx > mTouchSlop)
                    && ((mCurrentOpeningGravity == Gravity.TOP)
                        || (mCurrentOpeningGravity == Gravity.BOTTOM))
            );
        }
    }
    public boolean isInteractionStarted(float dx, float dy) {
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);
        return mCurrentOpeningParameters.isInteractionStarted(dx, dy, absDx, absDy, mTouchSlop);
    }
    public boolean shouldOpen(float velocityX, float velocityY) {
        return mCurrentOpeningParameters.shouldOpen(velocityX, velocityY, mOpenThreshold);
    }
    public boolean shouldFlyout(float velocityX, float velocityY) {
        return mCurrentOpeningParameters.shouldFlyout(velocityX, velocityY, mOpenThreshold);
    }

    public boolean setOpeningParametersByOffset(float dx, float dy) {
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);

        for (Map.Entry<Integer, InteractionParameters> pair: mAllOpeningParameters.entrySet()) {
            final Integer gravity = pair.getKey();
            final InteractionParameters openingParameters = pair.getValue();
            if (openingParameters.isInteractionStarted(dx, dy, absDx, absDy, mTouchSlop)) {
                mCurrentOpeningParameters.hideLeftBehindView();
                mCurrentOpeningGravity = gravity;
                mCurrentOpeningParameters = openingParameters;
                mCurrentOpeningParameters.showLeftBehindView();
                return true;
            }
        }
        return false;
    }
    public boolean setOpeningParametersByView(View view) {
        for (Map.Entry<Integer, InteractionParameters> pair: mAllOpeningParameters.entrySet()) {
            final Integer internalGravity = pair.getKey();
            final InteractionParameters openingParameters = pair.getValue();
            if (openingParameters.getLeftBehindView() == view) {
                mCurrentOpeningParameters.hideLeftBehindView();
                mCurrentOpeningGravity = internalGravity;
                mCurrentOpeningParameters = openingParameters;
                mCurrentOpeningParameters.showLeftBehindView();
                return true;
            }
        }
        return false;
    }
    public boolean setOpeningParametersByGravity(int gravity) {
        for (Map.Entry<Integer, InteractionParameters> pair: mAllOpeningParameters.entrySet()) {
            final Integer internalGravity = pair.getKey();
            final InteractionParameters openingParameters = pair.getValue();
            if (openingParameters.getLeftBehindGravity() == gravity) {
                mCurrentOpeningParameters.hideLeftBehindView();
                mCurrentOpeningGravity = internalGravity;
                mCurrentOpeningParameters = openingParameters;
                mCurrentOpeningParameters.showLeftBehindView();
                return true;
            }
        }
        return false;
    }
    public void resetOpeningParameters() {
        mCurrentOpeningParameters.hideLeftBehindView();
        mCurrentOpeningParameters = mAllOpeningParameters.get(EMPTY_OPENING_GRAVITY);
    }

    public void initialize(
            LeaveBehindLayout layout,
            int left, int top, int right, int bottom
    ) {
        mIsInitialized = true;
        this.clear();

        final LeftBehindViewAnimationProvider animationProvider = layout.getLeftBehindViewAnimationProvider();
        final int layoutDirection = ViewCompat.getLayoutDirection(layout);
        final int layoutLeft = layout.getPaddingLeft();
        final int layoutTop = layout.getPaddingTop();
        final int layoutRight = right - left - layout.getPaddingRight();
        final int layoutBottom = bottom - top - layout.getPaddingBottom();

        View foreView = null;
        int childCount = layout.getChildCount();
        for(int i = 0; i < childCount; ++i) {
            final View child = layout.getChildAt(i);

            final LeaveBehindLayout.LayoutParams lp = (LeaveBehindLayout.LayoutParams)child.getLayoutParams();
            final int absGravity = GravityCompat.getAbsoluteGravity(lp.gravity, layoutDirection);

            InteractionParameters interactionParameters;

            switch (absGravity) {
                case Gravity.NO_GRAVITY :
                    if (foreView != null) {
                        throw new IllegalStateException(
                                String.format(REPEATED_GRAVITY_MSG, getGravityString(absGravity)));
                    }
                    layoutForeView(child, lp, layoutLeft, layoutTop, layoutRight, layoutBottom);
                    foreView = child;
                    break;
                case Gravity.LEFT:
                    if (mAllOpeningParameters.containsKey(Gravity.LEFT)) {
                        throw new IllegalStateException(
                                String.format(REPEATED_GRAVITY_MSG, getGravityString(absGravity)));
                    }
                    layoutLeftLeaveBehindView(child, lp, layoutLeft, layoutTop, layoutRight, layoutBottom);
                    interactionParameters = new LeftInteractionParameters(child, lp.gravity, animationProvider.getLeftAnimation(), false/*lp.isOpenable*/, layout.isFlyoutable(lp.gravity));
                    mAllOpeningParameters.put(Gravity.LEFT, interactionParameters);
                    break;
                case Gravity.TOP:
                    if (mAllOpeningParameters.containsKey(Gravity.TOP)) {
                        throw new IllegalStateException(
                                String.format(REPEATED_GRAVITY_MSG, getGravityString(absGravity)));
                    }
                    layoutTopLeaveBehindView(child, lp, layoutLeft, layoutTop, layoutRight, layoutBottom);
                    interactionParameters = new TopInteractionParameters(child, lp.gravity, animationProvider.getTopAnimation(), lp.isOpenable, layout.isFlyoutable(lp.gravity));
                    mAllOpeningParameters.put(Gravity.TOP, interactionParameters);
                    break;
                case Gravity.RIGHT:
                    if (mAllOpeningParameters.containsKey(Gravity.RIGHT)) {
                        throw new IllegalStateException(
                                String.format(REPEATED_GRAVITY_MSG, getGravityString(absGravity)));
                    }
                    layoutRightLeaveBehindView(child, lp, layoutLeft, layoutTop, layoutRight, layoutBottom);
                    interactionParameters = new RightInteractionParameters(child, lp.gravity, animationProvider.getRightAnimation(), lp.isOpenable, layout.isFlyoutable(lp.gravity));
                    mAllOpeningParameters.put(Gravity.RIGHT, interactionParameters);
                    break;
                case Gravity.BOTTOM:
                    if (mAllOpeningParameters.containsKey(Gravity.BOTTOM)) {
                        throw new IllegalStateException(
                                String.format(REPEATED_GRAVITY_MSG, getGravityString(absGravity)));
                    }
                    layoutBottomLeaveBehindView(child, lp, layoutLeft, layoutTop, layoutRight, layoutBottom);
                    interactionParameters = new BottomInteractionParameters(child, lp.gravity, animationProvider.getBottomAnimation(), lp.isOpenable, layout.isFlyoutable(lp.gravity));
                    mAllOpeningParameters.put(Gravity.BOTTOM, interactionParameters);
                    break;
                default:
                    throw new IllegalStateException(INVALID_GRAVITY_MSG);
            }
        }
        if (foreView == null)
            throw new IllegalStateException(NULL_FORE_VIEW_MSG);

        for (Map.Entry<Integer, InteractionParameters> pair: mAllOpeningParameters.entrySet()) {
            final Integer gravity = pair.getKey();
            final InteractionParameters openingParameters = pair.getValue();
            openingParameters.setForeView(foreView);
            if (gravity.equals(mCurrentOpeningGravity)) {
                mCurrentOpeningParameters = openingParameters;
                openingParameters.showLeftBehindView();
            } else {
                openingParameters.hideLeftBehindView();
            }
        }
    }

    public void appendOffset(float offsetX, float offsetY) {
        final float totalOffset = mCurrentOpeningParameters.getCurrentOffset()
                + mCurrentOpeningParameters.selectValue(offsetX, offsetY);

        if (mCurrentOpeningParameters.isOffsetApplicable(totalOffset)) {
            mCurrentOpeningParameters.applyOffset(totalOffset);
        } else {
            final Integer oppositeGravity = getOppositeGravity(mCurrentOpeningGravity);
            final boolean hasOppositeParameters = mAllOpeningParameters.containsKey(oppositeGravity);
            if (hasOppositeParameters) {
                this.switchOpeningParametersToOpposite();
                mCurrentOpeningParameters.applyOffset(totalOffset);
            }
            else {
                mCurrentOpeningParameters.applyOffset(mCurrentOpeningParameters.getClosedOffset());
            }
        }
    }

    public void applyLeftBehindViewAnimation(float value) {
        mCurrentOpeningParameters.applyLeftBehindViewAnimation(value);
    }

    private String getGravityString(int gravity) {
        switch (gravity) {
            case Gravity.NO_GRAVITY: return "no_gravity";
            case Gravity.LEFT: return "left";
            case Gravity.TOP: return "top";
            case Gravity.RIGHT: return "right";
            case Gravity.BOTTOM: return "bottom";
        }
        return null;
    }

    private void clear() {
        mAllOpeningParameters.clear();
        mAllOpeningParameters.put(EMPTY_OPENING_GRAVITY, EMPTY_INTERACTION_PARAMETERS);
    }
    private void layoutForeView(
            View view, LeaveBehindLayout.LayoutParams layoutParams,
            int layoutLeft, int layoutTop, int layoutRight, int layoutBottom
    ) {
        final int left = layoutLeft + layoutParams.leftMargin;
        final int top = layoutTop + layoutParams.topMargin;
        final int right = left + view.getMeasuredWidth();
        final int bottom = top + view.getMeasuredHeight();
        view.layout(left, top, right, bottom);
    }
    private void layoutLeftLeaveBehindView(
            View view, LeaveBehindLayout.LayoutParams layoutParams,
            int layoutLeft, int layoutTop, int layoutRight, int layoutBottom
    ) {
        final int left = layoutLeft + layoutParams.leftMargin;
        final int top = layoutTop + layoutParams.topMargin;
        final int right = left + view.getMeasuredWidth();
        final int bottom = top + view.getMeasuredHeight();
        view.layout(left, top, right, bottom);
    }
    private void layoutTopLeaveBehindView(
            View view, LeaveBehindLayout.LayoutParams layoutParams,
            int layoutLeft, int layoutTop, int layoutRight, int layoutBottom
    ) {
        final int left = layoutLeft + layoutParams.leftMargin;
        final int top = layoutTop + layoutParams.topMargin;
        final int right = left + view.getMeasuredWidth();
        final int bottom = top + view.getMeasuredHeight();
        view.layout(left, top, right, bottom);
    }
    private void layoutRightLeaveBehindView(
            View view, LeaveBehindLayout.LayoutParams layoutParams,
            int layoutLeft, int layoutTop, int layoutRight, int layoutBottom
    ) {
        final int right = layoutRight - layoutParams.rightMargin;
        final int top = layoutTop + layoutParams.topMargin;
        final int left = right - view.getMeasuredWidth();
        final int bottom = top + view.getMeasuredHeight();
        view.layout(left, top, right, bottom);
    }
    private void layoutBottomLeaveBehindView(
            View view, LeaveBehindLayout.LayoutParams layoutParams,
            int layoutLeft, int layoutTop, int layoutRight, int layoutBottom
    ) {
        final int left = layoutLeft + layoutParams.leftMargin;
        final int bottom = layoutBottom - layoutParams.bottomMargin;
        final int right = left + view.getMeasuredWidth();
        final int top = bottom - view.getMeasuredHeight();
        view.layout(left, top, right, bottom);
    }

    private void switchOpeningParametersToOpposite() {
        Integer oppositeGravity = getOppositeGravity(mCurrentOpeningGravity);
        final boolean hasOppositeParameters = mAllOpeningParameters.containsKey(oppositeGravity);
        oppositeGravity = hasOppositeParameters ? oppositeGravity : EMPTY_OPENING_GRAVITY;

        mCurrentOpeningParameters.hideLeftBehindView();
        mCurrentOpeningGravity = oppositeGravity;
        mCurrentOpeningParameters = mAllOpeningParameters.get(oppositeGravity);
        mCurrentOpeningParameters.showLeftBehindView();
    }

    private static Integer getOppositeGravity(Integer gravity) {
        switch (gravity) {
            case Gravity.LEFT: return Gravity.RIGHT;
            case Gravity.TOP: return Gravity.BOTTOM;
            case Gravity.RIGHT: return Gravity.LEFT;
            case Gravity.BOTTOM: return Gravity.TOP;
            default: return EMPTY_OPENING_GRAVITY;
        }
    }
}