package com.romankarpov.leavebehindlayout.stateselectors;

import com.romankarpov.leavebehindlayout.LeaveBehindLayoutState;
import com.romankarpov.leavebehindlayout.core.InteractionModel;

public interface StateSelector {
    LeaveBehindLayoutState selectState(InteractionModel model, float velocity);
}
