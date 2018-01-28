package com.romankarpov.leavebehindlayout;

import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.romankarpov.leavebehindlayout.physics.PhysicsAnimationConfig;
import com.romankarpov.leavebehindlayout.physics.PhysicsObject;
import com.romankarpov.leavebehindlayout.physics.ReboundPhysicsBehavior;
import com.romankarpov.leavebehindlayout.physics.SpringPhysicsBehavior;

class FlyoutLayoutState implements LeaveBehindLayoutState {
    @Override
    public int getFlag() {
        return LeaveBehindLayout.FLAG_FLYOUT;
    }

    @Override
    public void applyLayout(LeaveBehindLayout layout) {

    }

    @Override
    public boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        return false;
    }

    @Override
    public boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        return false;
    }

    @Override
    public PhysicsAnimationConfig createAnimationConfig(LeaveBehindLayout layout) {
        final LeaveBehindLayoutConfig config = layout.getConfig();
        final VelocityTracker velocityTracker = layout.getVelocityTracker();

        final float positionX = config.getCurrentPositionX();
        final float positionY = config.getCurrentPositionY();
        final float velocityX = config.clipVelocityX(
                velocityTracker.getXVelocity());
        final float velocityY = config.clipVelocityY(
                velocityTracker.getYVelocity());

        final float lastPositionX = config.getFlyoutPositionX();
        final float lastPositionY = config.getFlyoutPositionY();

        final PhysicsObject object =  new PhysicsObject(1, velocityX, velocityY, positionX, positionY);
        final PhysicsAnimationConfig.Callback callback = new AnimatorCallback(layout, this);

        final PhysicsAnimationConfig animationConfig = new PhysicsAnimationConfig(object, callback);
        animationConfig.addPhysicsBehavior(new SpringPhysicsBehavior(150, lastPositionX, lastPositionY));
        animationConfig.addPhysicsBehavior(new ReboundPhysicsBehavior(0.f, lastPositionX, lastPositionY));

        return animationConfig;
    }

    class AnimatorCallback extends AbstractAnimatorCallback {
        AnimatorCallback(LeaveBehindLayout layout, LeaveBehindLayoutState terminalState) {
            super(layout, terminalState);
        }

        @Override
        protected void notifyListenersOnProgress() {
            final LeaveBehindLayout layout = this.getLayout();
            final LeaveBehindLayoutConfig config = layout.getConfig();
            layout.dispatchFlyingOut(
                    config.getLeftBehindGravity(),
                    config.calculateFlyoutProgress()
            );
        }

        @Override
        protected void notifyListenersOnFinish() {
            final LeaveBehindLayout layout = this.getLayout();
            final LeaveBehindLayoutConfig config = layout.getConfig();
            layout.dispatchFlewOut(config.getLeftBehindGravity());
        }
    }
}
