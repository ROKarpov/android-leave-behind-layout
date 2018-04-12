package com.romankarpov.leavebehindlayout;

class UnflyoutableBehavior implements FlyoutBehavior {
    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public boolean isOffsetApplicable(float offset) {
        return false;
    }
}
