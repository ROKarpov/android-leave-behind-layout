package com.romankarpov.leavebehindlayout.physics;

public class ReboundPhysicsBehavior implements PhysicsBehavior{
    float mEmitCoefficient;
    float mAnchorPointX;
    float mAnchorPointY;
    boolean mShouldBeApplied;

    public ReboundPhysicsBehavior(float emitCoefficient, float anchorPointX, float anchorPointY){
        this.mEmitCoefficient = emitCoefficient;
        this.mAnchorPointX = anchorPointX;
        this.mAnchorPointY = anchorPointY;
        this.mShouldBeApplied = false;
    }

    @Override
    public void updatePhysicalObject(PhysicsObject object, float dt) {
        float currPositionX = object.getPositionX();
        float currPositionY = object.getPositionY();
        float nextPositionX = currPositionX + object.getVelocityX() * dt;
        float nextPositionY = currPositionY + object.getVelocityY() * dt;

        float buff;
        if (currPositionX > nextPositionX) {
            buff = nextPositionX;
            nextPositionX = currPositionX;
            currPositionX = buff;
        }
        if (currPositionY > nextPositionY) {
            buff = nextPositionY;
            nextPositionY = currPositionY;
            currPositionY = buff;
        }

        if (((currPositionX < mAnchorPointX) && (mAnchorPointX < nextPositionX))
                || ((currPositionY < mAnchorPointY) && (mAnchorPointY < nextPositionY))) {
            object.setVelocityX(-object.getVelocityX() * mEmitCoefficient);
            object.setVelocityY(-object.getVelocityY() * mEmitCoefficient);
            object.setPositionX(mAnchorPointX);
            object.setPositionY(mAnchorPointY);
        }
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
