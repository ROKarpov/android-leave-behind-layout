package com.romankarpov.leavebehindlayout.physics;


public class FrictionPhysicsBehavior implements PhysicsBehavior {
    float mFriction;

    public FrictionPhysicsBehavior(float friction) {
        if (friction <= 0 && friction > 1) {
            throw new IllegalStateException("The friction value must be in the [0, 1) interval.");
        }
        mFriction = friction;
    }

    @Override
    public void updatePhysicalObject(PhysicsObject object, float dt) {
        object.setVelocityX(object.getVelocityX() * mFriction);
        object.setVelocityY(object.getVelocityY() * mFriction);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean isEnded(PhysicsObject object) {
        return true;
    }
}
