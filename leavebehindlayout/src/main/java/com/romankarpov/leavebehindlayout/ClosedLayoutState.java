package com.romankarpov.leavebehindlayout;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.romankarpov.leavebehindlayout.physics.FrictionPhysicsBehavior;
import com.romankarpov.leavebehindlayout.physics.PhysicsAnimationConfig;
import com.romankarpov.leavebehindlayout.physics.PhysicsObject;
import com.romankarpov.leavebehindlayout.physics.SpringPhysicsBehavior;

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
    public PhysicsAnimationConfig createAnimationConfig(LeaveBehindLayout layout) {
        final LeaveBehindLayoutConfig config = layout.getConfig();
        final VelocityTracker velocityTracker = layout.getVelocityTracker();

        final float positionX = config.getCurrentPositionX();
        final float positionY = config.getCurrentPositionY();
        final float velocityX = (velocityTracker != null)
                ? config.clipVelocityX(velocityTracker.getXVelocity())
                : 0f;
        final float velocityY = (velocityTracker != null)
                ? config.clipVelocityY(velocityTracker.getYVelocity())
                : 0f;

        final float lastPositionX = config.getClosedPositionX();
        final float lastPositionY = config.getClosedPositionY();
        final PhysicsObject object =  new PhysicsObject(1, velocityX, velocityY, positionX, positionY);

        final PhysicsAnimationConfig.Callback callback = new AnimatorCallback(layout, this);


        final PhysicsAnimationConfig animationConfig = new PhysicsAnimationConfig(object, callback);
        animationConfig.addPhysicsBehavior(new SpringPhysicsBehavior(300, lastPositionX, lastPositionY));
        animationConfig.addPhysicsBehavior(new FrictionPhysicsBehavior(0.6f));

        return animationConfig;
    }

    class AnimatorCallback extends AbstractAnimatorCallback {
        AnimatorCallback(LeaveBehindLayout layout, LeaveBehindLayoutState terminalState) {
            super(layout, terminalState);
        }

        @Override
        protected void notifyListenersOnFinish() {
            final LeaveBehindLayout layout = this.getLayout();
            final LeaveBehindLayoutConfig config = layout.getConfig();
            layout.dispatchLeaveBehindClosed(config.getLeftBehindGravity(), config.getLeftBehindView());
        }

        @Override
        protected void notifyListenersOnProgress() {
            final LeaveBehindLayout layout = this.getLayout();
            final LeaveBehindLayoutConfig config = layout.getConfig();
            layout.dispatchLeaveBehindOpeningProgress(
                    config.getLeftBehindGravity(),
                    config.getLeftBehindView(),
                    config.calculateOpenProgress()
            );
        }

        @Override
        public void onAnimationFinished(PhysicsAnimationConfig animationConfig) {
            super.onAnimationFinished(animationConfig);
            this.getLayout().getConfig().resetOpeningParameters();
        }
    }
}
