package com.romankarpov.leavebehindlayout.physics;

import android.view.Choreographer;

import org.jetbrains.annotations.NotNull;

public class PhysicsAnimator implements Choreographer.FrameCallback {
    static final long EMPTY_TIME = Long.MIN_VALUE;

    private Choreographer mChoreographer;
    private final Object locker = new Object();
    private long mPrevTime = EMPTY_TIME;


    PhysicsAnimationConfig mCurrentConfig = null;


    public PhysicsAnimator() {
        mChoreographer = Choreographer.getInstance();
    }

    public void start(@NotNull PhysicsAnimationConfig animationConfig) {
        synchronized (locker) {
            if (mCurrentConfig != null) {
                //mCurrentConfig.endAnimation();
                mPrevTime = EMPTY_TIME;
            }
            mCurrentConfig = animationConfig;
            mChoreographer.postFrameCallback(this);
        }
    }
    public void end() {
        synchronized (locker) {

        }
    }

    @Override
    public  void doFrame(long frameTimeNanos) {
        synchronized (locker) {
            if (mCurrentConfig != null) {
                if (mPrevTime != EMPTY_TIME) {
                    final float dt = ((float) (frameTimeNanos - mPrevTime)) / 1000000000;
                    final PhysicsObject object = mCurrentConfig.getObject();
                    for (PhysicsBehavior behavior : mCurrentConfig.getPhysicsBehaviors()) {
                        behavior.updatePhysicalObject(object, dt);
                    }

                    final float newPositionX = object.getPositionX() + object.getVelocityX() * dt;
                    final float newPositionY = object.getPositionY() + object.getVelocityY() * dt;

                    object.setPositionX(newPositionX);
                    object.setPositionY(newPositionY);

                    mCurrentConfig.applyState();
                }
                mPrevTime = frameTimeNanos;

                if (mCurrentConfig.isEnded()) {
                    mCurrentConfig.endAnimation();
                    mCurrentConfig = null;
                    mPrevTime = EMPTY_TIME;
                } else {
                    mChoreographer.postFrameCallback(this);
                }
            }
        }
    }
}