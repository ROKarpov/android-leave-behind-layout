package com.romankarpov.leavebehindlayout;

class SwitchToOppositeOffsetBehavior implements UnavailableOffsetBehavior {
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
