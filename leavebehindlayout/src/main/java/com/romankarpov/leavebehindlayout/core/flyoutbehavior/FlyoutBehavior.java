package com.romankarpov.leavebehindlayout.core.flyoutbehavior;


public interface FlyoutBehavior {
    boolean isAvailable();
    boolean isOffsetApplicable(float offset);
}