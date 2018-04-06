package com.romankarpov.leavebehindlayout;

import android.support.animation.DynamicAnimation;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.romankarpov.leavebehindlayout.core.InteractionModel;


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
        final InteractionModel model = layout.getActualInteractionModel();

        final int actionIndex = layout.getActionIndex();
        final int pointerId = event.getPointerId(actionIndex);
        final float newX = event.getX(pointerId);
        final float newY = event.getY(pointerId);
        final float dx = newX - layout.getMotionLastX();
        final float dy = newY - layout.getMotionLastY();
        final float offset = model.selectValue(dx, dy);
        model.appendOffset(offset);

        layout.getVelocityTracker().addMovement(event);
        layout.updateLastPosition(newX, newY);

        return true;
    }

    boolean handleUp(LeaveBehindLayout layout, MotionEvent event) {
        final InteractionModel model = layout.getActualInteractionModel();

        final int pointerId = event.getPointerId(layout.getActionIndex());
        final float newX = event.getX(pointerId);
        final float newY = event.getY(pointerId);
        final float dx = newX - layout.getMotionLastX();
        final float dy = newY - layout.getMotionLastY();
        final float offset = model.selectValue(dx, dy);
        model.appendOffset(offset);

        final VelocityTracker velocityTracker = layout.getVelocityTracker();
        velocityTracker.computeCurrentVelocity(1000);
        final float velocity = model.getVelocityFrom(velocityTracker);
        final LeaveBehindLayoutState nextState = layout.getStateSelector().selectState(model, velocity);

        layout.setState(nextState);
        layout.startAnimationToCurrentState();
        layout.endTrackEvent();
        return true;
    }

    @Override
    public void applyLayout(LeaveBehindLayout layout) {
        InteractionModel config = layout.getActualInteractionModel();
        config.applyOffset(config.getFlewOutPosition());
        layout.invalidate();
    }

    @Override
    public float getFinalPositionFrom(InteractionModel model) {
        throw new IllegalStateException("The \'getFinalPositionFrom\' method is not applicable in this state.");
    }

    @Override
    public DynamicAnimation.OnAnimationUpdateListener getAnimationUpdateListener(LeaveBehindLayout layout) {
        throw new IllegalStateException("The \'getAnimationUpdateListener\' method is not applicable in this state.");
    }

    @Override
    public DynamicAnimation.OnAnimationEndListener getAnimationEndListener(LeaveBehindLayout layout) {
        throw new IllegalStateException("The \'getAnimationEndListener\' method is not applicable in this state.");
    }
}
