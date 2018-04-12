package com.romankarpov.leavebehindlayout;

import com.romankarpov.leavebehindlayout.InteractionModel;
import com.romankarpov.leavebehindlayout.LeaveBehindLayoutState;

public interface StateSelector {
    LeaveBehindLayoutState selectState(InteractionModel model, float velocity);
}
