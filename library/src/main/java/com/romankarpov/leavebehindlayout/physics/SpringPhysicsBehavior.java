package com.romankarpov.leavebehindlayout.physics;


public class SpringPhysicsBehavior implements PhysicsBehavior {
    static float EPS_VELOCITY = 1f;
    static float EPS_DISTANCE = 1f;

    float mTension;
    float mAnchorPointX;
    float mAnchorPointY;

    public SpringPhysicsBehavior(float tension, float anchorPointX, float anchorPointY){
        this.mTension = tension;
        this.mAnchorPointX = anchorPointX;
        this.mAnchorPointY = anchorPointY;
    }

    @Override
    public void updatePhysicalObject(PhysicsObject object, float dt) {
        final float distanceX = this.mAnchorPointX - object.getPositionX();
        final float aX = mTension * distanceX * object.getInvMass();
        object.setVelocityX(object.getVelocityX() + aX * dt);

        final float distanceY = this.mAnchorPointY - object.getPositionY();
        final float aY = mTension * distanceY * object.getInvMass();
        object.setVelocityY(object.getVelocityY() + aY * dt);
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    @Override
    public boolean isEnded(PhysicsObject object) {
        final float velocityX = object.getVelocityX();
        final float velocityY = object.getVelocityY();
        final float positionX = object.getPositionX();
        final float positionY = object.getPositionY();
        final float distanceX = (positionX - mAnchorPointX);
        final float distanceY = (positionY - mAnchorPointY);

        final boolean isZeroVelocity = ((velocityX < EPS_VELOCITY) && (velocityX > -EPS_VELOCITY)
                && (velocityY < EPS_VELOCITY) && (velocityY > -EPS_VELOCITY));
        final boolean isZeroDistance = ((distanceX < EPS_DISTANCE) && (distanceX > -EPS_DISTANCE)
                && (distanceY < EPS_DISTANCE) && (distanceY > -EPS_DISTANCE));

        return isZeroDistance && isZeroVelocity;
    }
}
