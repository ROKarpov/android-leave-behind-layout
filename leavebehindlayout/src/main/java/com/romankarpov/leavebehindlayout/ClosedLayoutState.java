package com.romankarpov.leavebehindlayout;

import android.support.animation.DynamicAnimation;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

class ClosedLayoutState implements LeaveBehindLayoutState {
    @Override
    public int getFlag() {
        return LeaveBehindLayout.FLAG_CLOSED;
    }

    @Override
    public void applyLayout(LeaveBehindLayout layout) {
        LeaveBehindLayoutConfig config = layout.getConfig();
        config.trySetOffset(config.getClosedPositionX(), config.getClosedPositionY());
        layout.invalidate();
    }

    @Override
    public boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return handleDown(layout, event);
            case MotionEvent.ACTION_MOVE:
                return handleMove(layout, event);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Reset this flag for next time.
                Log.d("Closed State", "Handle Up");
                layout.endTrackEvent();
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return shouldInterceptDown(layout, event);
            case MotionEvent.ACTION_MOVE:
                return handleMove(layout, event);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d("Closed State", "Should Intercept Up");
                layout.endTrackEvent();
                return true;
            default:
                return false;
        }
    }

    boolean shouldInterceptDown(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Closed State", "Should Intercept Down");
        final LeaveBehindLayoutConfig config = layout.getConfig();
        final int actionIndex = event.getActionIndex();
        final float x = event.getX(actionIndex);
        final float y = event.getY(actionIndex);

        final boolean shouldTrackEvent = config.isPointInForeView(x, y);
        if (shouldTrackEvent) {
            layout.startTrackEvent(actionIndex, x, y);
        }

        return shouldTrackEvent;
    }

    boolean handleDown(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Closed State", "Handle Down");
        final int actionIndex = layout.getActionIndex();
        if (event.getActionIndex() != actionIndex) return false;

        return layout.isEventTracked();
    }

    boolean handleMove(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Closed State", "Handle Move");
        final LeaveBehindLayoutConfig config = layout.getConfig();
        final int actionIndex = layout.getActionIndex();
        if (event.getActionIndex() != actionIndex) return false;

        final int motionId = event.getPointerId(actionIndex);
        final float x = event.getX(motionId);
        final float y = event.getY(motionId);

        final float dx = x - layout.getMotionInitialX();
        final float dy = y - layout.getMotionInitialY();

        Log.d ("ClosedLayout", String.format("%f %f", dx, dy));
        if (config.canSelectOpeningParameters(dx, dy)) {
            if (config.setOpeningParametersByOffset(dx, dy)) {
                layout.getParent().requestDisallowInterceptTouchEvent(true);
                layout.setState(LeaveBehindLayout.DRAGGING_STATE);
                layout.updateLastPosition(x, y);
                //layout.notifyInteractionStarted(config.getLeftBehindGravity(), config.getLeftBehindView());
            }
            return false;
        }
        return true;
    }

    @Override
    public float getFinalPositionFrom(LeaveBehindLayoutConfig config) {
        return config.getClosedPosition();
    }

    @Override
    public DynamicAnimation.OnAnimationUpdateListener getAnimationUpdateListener(LeaveBehindLayout layout) {
        return new AnimationUpdateListener(layout);
    }

    @Override
    public DynamicAnimation.OnAnimationEndListener getAnimationEndListener(LeaveBehindLayout layout) {
        return new AnimationEndListener(layout);
    }

    class AnimationUpdateListener implements DynamicAnimation.OnAnimationUpdateListener {
        LeaveBehindLayout mLayout;

        public AnimationUpdateListener(LeaveBehindLayout layout) {
            mLayout = layout;
        }

        @Override
        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
            final LeaveBehindLayoutConfig config = mLayout.getConfig();
            final int gravity = config.getLeftBehindGravity();
            final View view = config.getLeftBehindView();
            final float progress = config.calculateOpeningProgress();
            mLayout.dispatchLeaveBehindOpeningProgress(gravity, view, progress);
        }
    }

    class AnimationEndListener implements DynamicAnimation.OnAnimationEndListener {
        private LeaveBehindLayout mLayout;

        public AnimationEndListener(LeaveBehindLayout layout) {
            mLayout = layout;
        }
        @Override
        public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
            final LeaveBehindLayoutConfig config = mLayout.getConfig();
            mLayout.dispatchLeaveBehindClosed(config.getLeftBehindGravity(), config.getLeftBehindView());
        }
    }
}
