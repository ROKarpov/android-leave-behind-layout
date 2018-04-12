package com.romankarpov.leavebehindlayout;

class ZeroOffsetUnavailableOffsetBehavior implements UnavailableOffsetBehavior {
    @Override
    public void onUnavailableOffset(AbstractInteractionModel interactionModel, float offset) {
        interactionModel.applyOffset(0.f);
    }
}
