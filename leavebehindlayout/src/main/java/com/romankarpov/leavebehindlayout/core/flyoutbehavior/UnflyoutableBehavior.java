package com.romankarpov.leavebehindlayout.core.flyoutbehavior;

public class UnflyoutableBehavior implements FlyoutBehavior {
    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isOffsetApplicable(float offset) {
        return false;
    }
}
