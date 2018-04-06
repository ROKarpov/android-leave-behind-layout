package com.romankarpov.leavebehindlayout.core.unavailableoffsetbehavior;

import com.romankarpov.leavebehindlayout.core.AbstractInteractionModel;

public class ZeroOffsetUnavailableOffsetBehavior implements UnavailableOffsetBehavior {
    @Override
    public void onUnavailableOffset(AbstractInteractionModel interactionModel, float offset) {
        interactionModel.applyOffset(0.f);
    }
}
