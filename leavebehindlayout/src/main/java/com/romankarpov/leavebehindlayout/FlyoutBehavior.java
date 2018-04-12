package com.romankarpov.leavebehindlayout;


interface FlyoutBehavior {
    boolean isAvailable();
    boolean isOffsetApplicable(float offset);
}