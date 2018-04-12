package com.romankarpov.leavebehindlayout;

import android.support.animation.DynamicAnimation;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;


class OpenedLayoutState implements LeaveBehindLayoutState {
    @Override
    public final void applyLayout(LeaveBehindLayout layout) {
        InteractionModel model = layout.getActualInteractionModel();
        model.applyOffset(model.getOpenedPosition());
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
                handleUp(layout, event);
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
                return handleUp(layout, event);
            default:
                return false;
        }
    }

    boolean shouldInterceptDown(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Opened State", "Should Intercept Down");
        final InteractionModel model = layout.getActualInteractionModel();
        final int actionIndex = event.getActionIndex();
        final float x = event.getX(actionIndex);
        final float y = event.getY(actionIndex);

        final boolean shouldTrackEvent = model.isPointInForeView(x, y);
        if (shouldTrackEvent) {
            layout.startTrackEvent(actionIndex, x, y);
        }
        return shouldTrackEvent;
    }

    boolean handleDown(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Opened State", "Handle Down");
        final int actionIndex = layout.getActionIndex();
        if (event.getActionIndex() != actionIndex) return false;

        return layout.isEventTracked();
    }

    boolean handleMove(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Opened State", "Handle Move");
        final InteractionModel model = layout.getActualInteractionModel();
        final int actionIndex = layout.getActionIndex();
        if ((event.getActionIndex() != actionIndex) || !layout.isEventTracked()) return false;

        final int pointerIndex = event.getPointerId(actionIndex);
        final float x = event.getX(pointerIndex);
        final float y = event.getY(pointerIndex);

        boolean shouldTrackEvent = false;

        final float dx = x - layout.getMotionInitialX();
        final float dy = y - layout.getMotionInitialY();


        if (isTargetOffset(model.getGravity(), dx, dy, layout.getTouchSlop())) {
            layout.setState(LeaveBehindLayout.DRAGGING_STATE);
            layout.getParent().requestDisallowInterceptTouchEvent(true);
            shouldTrackEvent = true;
        }
        layout.updateLastPosition(x, y);
        return shouldTrackEvent;
    }

    boolean handleUp(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Opened State", "Handle Up");
        final int currentEventIndex = event.getActionIndex();
        final int trackingEventIndex = layout.getActionIndex();
        if ((currentEventIndex == trackingEventIndex) && layout.isEventTracked()) {
            layout.setState(LeaveBehindLayout.CLOSED_STATE);
            layout.startAnimationToCurrentState();
            layout.endTrackEvent();
            return true;
        }
        return false;
    }

    @Override
    public float getFinalPositionFrom(InteractionModel model) {
        return model.getOpenedPosition();
    }

    @Override
    public DynamicAnimation.OnAnimationUpdateListener getAnimationUpdateListener(LeaveBehindLayout layout) {
        return new AnimationUpdateListener(layout);
    }

    @Override
    public DynamicAnimation.OnAnimationEndListener getAnimationEndListener(LeaveBehindLayout layout) {
        return new AnimationEndListener(layout);
    }

    boolean isTargetOffset(int gravity, float dx, float dy, float touchSlop) {
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);
        final boolean isOffsetHorizontal = absDx > absDy;
        if (isOffsetHorizontal) {
            return ((absDx > touchSlop)
                    && ((gravity == Gravity.LEFT)
                    || (gravity == Gravity.RIGHT))
            );
        } else {
            return ((absDx > touchSlop)
                    && ((gravity == Gravity.TOP)
                    || (gravity == Gravity.BOTTOM))
            );
        }
    }

    @Override
    public void onStateSpecified(LeaveBehindLayout layout) {
        // TODO: IMPLEMENT!
    }

    class AnimationUpdateListener implements DynamicAnimation.OnAnimationUpdateListener {
        LeaveBehindLayout mLayout;

        public AnimationUpdateListener(LeaveBehindLayout layout) {
            mLayout = layout;
        }

        @Override
        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
            final InteractionModel model = mLayout.getActualInteractionModel();
            model.animateLeftBehindView(value);
            final int gravity = model.getGravity();
            final View view = model.getLeftBehindView();
            final float progress = model.getOpeningProgress();
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
            final InteractionModel model = mLayout.getActualInteractionModel();
            mLayout.dispatchLeaveBehindOpened(model.getGravity(), model.getLeftBehindView());
        }
    }
}



