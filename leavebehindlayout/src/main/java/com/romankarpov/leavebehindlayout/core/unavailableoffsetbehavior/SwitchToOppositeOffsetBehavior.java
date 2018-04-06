package com.romankarpov.leavebehindlayout.core.unavailableoffsetbehavior;

import com.romankarpov.leavebehindlayout.LeaveBehindLayout;
import com.romankarpov.leavebehindlayout.core.AbstractInteractionModel;
import com.romankarpov.leavebehindlayout.core.InteractionModel;

public class SwitchToOppositeOffsetBehavior implements UnavailableOffsetBehavior {
    LeaveBehindLayout mLayout;
    InteractionModel mOppositeModel;
    public SwitchToOppositeOffsetBehavior(
            LeaveBehindLayout layout,
            InteractionModel oppositeInteractionModel) {
        mLayout = layout;
        mOppositeModel = oppositeInteractionModel;
    }

    @Override
    public void onUnavailableOffset(AbstractInteractionModel interactionModel, float offset) {
        mLayout.setActualInteractionModel(mOppositeModel);
        mLayout.getActualInteractionModel().applyOffset(offset);
    }
}
