package com.romankarpov.leavebehindlayout.physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsAnimationConfig {
    private PhysicsObject mObject;
    private List<PhysicsBehavior> mPhysicsBehaviors;
    private Callback mCallback;

    public PhysicsAnimationConfig(
            PhysicsObject physicsObject,
            Callback callback
    ) {
        mObject = physicsObject;
        mPhysicsBehaviors = new ArrayList<>();
        mCallback = callback;
    }

    public PhysicsObject getObject() {
        return mObject;
    }

    public void addPhysicsBehavior(PhysicsBehavior behavior) {
        for (int i = 0; i < mPhysicsBehaviors.size(); ++i) {
            if (behavior.getPriority() > mPhysicsBehaviors.get(i).getPriority()) {
                mPhysicsBehaviors.add(i, behavior);
                return;
            }
        }
        mPhysicsBehaviors.add(behavior);
    }
    public void removePhysicsBehavior(PhysicsBehavior behavior) {
        mPhysicsBehaviors.remove(behavior);
    }

    public void applyState() {
        mCallback.onDoFrame(this);
    }

    public void endAnimation() {
        mCallback.onAnimationFinished(this);
    }

    public boolean isEnded() {
        boolean isEnded = true;
        for (PhysicsBehavior behavior: mPhysicsBehaviors) {
            isEnded = behavior.isEnded(mObject) && isEnded;
        }
        return isEnded;
    }

    List<PhysicsBehavior> getPhysicsBehaviors() {
        return mPhysicsBehaviors;
    }

    public interface Callback {
        void onDoFrame(PhysicsAnimationConfig config);
        void onAnimationFinished(PhysicsAnimationConfig config);
    }
}
