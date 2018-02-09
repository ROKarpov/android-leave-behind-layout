package com.romankarpov.leavebehindlayout;

import android.util.Log;
import android.view.MotionEvent;

class OpenedLayoutState implements LeaveBehindLayoutState {
    @Override
    public int getFlag() {
        return LeaveBehindLayout.FLAG_OPENED;
    }

    @Override
    public final void applyLayout(LeaveBehindLayout layout) {
        LeaveBehindLayoutConfig config = layout.getConfig();
        config.trySetOffset(config.getOpenedPositionX(), config.getOpenedPositionY());
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
        Log.d("Opened State", "Handle Down");
        final int actionIndex = layout.getActionIndex();
        if (event.getActionIndex() != actionIndex) return false;

        return layout.isEventTracked();
    }

    boolean handleMove(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Opened State", "Handle Move");
        final LeaveBehindLayoutConfig config = layout.getConfig();
        final int actionIndex = layout.getActionIndex();
        if ((event.getActionIndex() != actionIndex) || !layout.isEventTracked()) return false;

        final int pointerIndex = event.getPointerId(actionIndex);
        final float x = event.getX(pointerIndex);
        final float y = event.getY(pointerIndex);

        boolean shouldTrackEvent = false;

        final float dx = x - layout.getMotionInitialX();
        final float dy = y - layout.getMotionInitialY();

        if (config.isTargetOffset(dx, dy)) {
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
    public float getFinalPositionFrom(LeaveBehindLayoutConfig config) {
        return config.getOpenedPosition();
    }
}



