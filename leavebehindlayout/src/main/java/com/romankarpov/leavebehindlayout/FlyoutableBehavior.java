package com.romankarpov.leavebehindlayout;

class FlyoutableBehavior implements FlyoutBehavior {
    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean isOffsetApplicable(float offset) {
        return false;
    }
}
