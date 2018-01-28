package com.romankarpov.leavebehindlayout.animations;

public interface LeftBehindViewAnimationProvider {
    LeftBehindViewAnimation getLeftAnimation();
    LeftBehindViewAnimation getTopAnimation();
    LeftBehindViewAnimation getRightAnimation();
    LeftBehindViewAnimation getBottomAnimation();
}
