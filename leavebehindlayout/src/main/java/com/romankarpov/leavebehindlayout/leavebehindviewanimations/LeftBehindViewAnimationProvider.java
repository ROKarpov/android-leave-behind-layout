package com.romankarpov.leavebehindlayout.leavebehindviewanimations;

public interface LeftBehindViewAnimationProvider {
    LeftBehindViewAnimation getLeftAnimation();
    LeftBehindViewAnimation getTopAnimation();
    LeftBehindViewAnimation getRightAnimation();
    LeftBehindViewAnimation getBottomAnimation();
}
