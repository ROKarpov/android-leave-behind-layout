package com.romankarpov.leavebehindlayout.physics;


public class PhysicsObject {
    float mMass;
    float mInvMass;
    float mVelocityX;
    float mVelocityY;
    float mPositionX;
    float mPositionY;

    public PhysicsObject() {
        mMass = 1.f;
        mInvMass = 1.f;
        mVelocityX = 0;
        mVelocityY = 0;
        mPositionX = 0;
        mPositionY = 0;
    }
    public PhysicsObject(float mass, float velocityX, float velocityY, float positionX, float positionY) {
        mMass = mass;
        mInvMass = 1.f / mass;
        mVelocityX = velocityX;
        mVelocityY = velocityY;
        mPositionX = positionX;
        mPositionY = positionY;
    }

    public float getMass() {
        return this.mMass;
    }
    public float getInvMass() {
        return mInvMass;
    }
    public void setMass(float mass) {
        mMass = mass;
        mInvMass = 1.f / mass;
    }

    public float getVelocityX() {
        return mVelocityX;
    }
    public void setVelocityX(float velocityX) {
        mVelocityX = velocityX;
    }

    public float getVelocityY() {
        return mVelocityY;
    }
    public void setVelocityY(float velocityY) {
        mVelocityY = velocityY;
    }

    public float getPositionX() {
        return mPositionX;
    }
    public void setPositionX(float positionX) {
        mPositionX = positionX;
    }

    public float getPositionY() {
        return mPositionY;
    }
    public void setPositionY(float positionY) {
        mPositionY = positionY;
    }
}
