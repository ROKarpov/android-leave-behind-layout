package com.romankarpov.leavebehindlayout.physics;


public interface PhysicsBehavior {
    void updatePhysicalObject(PhysicsObject object, float dt);
    int getPriority();
    boolean isEnded(PhysicsObject object);
}
