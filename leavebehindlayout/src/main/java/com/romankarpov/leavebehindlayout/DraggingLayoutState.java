package com.romankarpov.leavebehindlayout;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.romankarpov.leavebehindlayout.physics.PhysicsAnimationConfig;


class DraggingLayoutState implements LeaveBehindLayoutState {
    @Override
    public int getFlag() {
        return LeaveBehindLayout.FLAG_DRAGGING;
    }

    @Override
    public boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        return false;
    }

    @Override
    public boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        if ((event.getActionIndex() != layout.getActionIndex()) || !layout.isEventTracked()) {
            return false;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return false;
            case MotionEvent.ACTION_MOVE:
                return handleMove(layout, event);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return handleUp(layout, event);
            default:
                throw new IllegalStateException("The layout in this state must not handle the onTouchEvent event with an action.");
        }
    }

    boolean handleMove(LeaveBehindLayout layout, MotionEvent event) {
        final LeaveBehindLayoutConfig config = layout.getConfig();
        final int actionIndex = layout.getActionIndex();
        final int pointerId = event.getPointerId(actionIndex);
        final float newX = event.getX(pointerId);
        final float newY = event.getY(pointerId);
        final float dx = newX - layout.getMotionLastX();
        final float dy = newY - layout.getMotionLastY();

        if (!config.tryAppendOffset(dx, dy)) {
            config.switchOpeningParametersToOpposite(dx, dy);
            config.tryAppendOffset(dx, dy);
        }
        layout.getVelocityTracker().addMovement(event);
        layout.updateLastPosition(newX, newY);
        layout.invalidate();
        return true;
    }

    boolean handleUp(LeaveBehindLayout layout, MotionEvent event) {
        final LeaveBehindLayoutConfig config = layout.getConfig();
        final VelocityTracker velocityTracker = layout.getVelocityTracker();

        final int pointerId = event.getPointerId(layout.getActionIndex());
        final float newX = event.getX(pointerId);
        final float newY = event.getY(pointerId);
        final float dx = newX - layout.getMotionLastX();
        final float dy = newY - layout.getMotionLastY();

        if (!config.tryAppendOffset(dx, dy)) {
            config.switchOpeningParametersToOpposite(dx, dy);
            config.tryAppendOffset(dx, dy);
        }

        velocityTracker.computeCurrentVelocity(1000);
        final float velocityX = config.clipVelocityX(velocityTracker.getXVelocity(pointerId));
        final float velocityY = config.clipVelocityY(velocityTracker.getYVelocity(pointerId));

        LeaveBehindLayoutState nextState = LeaveBehindLayout.CLOSED_STATE;
        if (config.shouldOpen(velocityX, velocityY)) {
            nextState = LeaveBehindLayout.OPENED_STATE;
        } else if (config.shouldFlyout(velocityX, velocityY)) {
            nextState = LeaveBehindLayout.FLYOUT_STATE;
        }
        layout.setState(nextState);
        layout.startAnimationToCurrentState();
        layout.endTrackEvent();
        return true;
    }

    @Override
    public void applyLayout(LeaveBehindLayout layout) {
        throw new IllegalStateException("The applyLayout method is not applicable in this state.");
    }

    @Override
    public PhysicsAnimationConfig createAnimationConfig(LeaveBehindLayout layout) {
        throw new IllegalStateException("");
    }
}
