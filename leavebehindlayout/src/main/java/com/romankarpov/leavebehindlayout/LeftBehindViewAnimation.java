package com.romankarpov.leavebehindlayout;

import android.view.View;

public interface LeftBehindViewAnimation {
    void applyLeft(View view, float value);
    void applyTop(View view, float value);
    void applyRight(View view, float value);
    void applyBottom(View view, float value);
}