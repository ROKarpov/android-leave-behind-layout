package com.romankarpov.leavebehindlayout.core;

import android.support.animation.DynamicAnimation;
import android.view.VelocityTracker;
import android.view.View;

public interface InteractionModel {
    void appendOffset(float offset);
    void applyOffset(float offset);

    int getGravity();

    float getClosedPosition();
    float getCurrentPosition();
    float getOpenedPosition();
    float getFlewOutPosition();
    float getOpeningProgress();
    float getFlyingOutProgress();

    void startInteraction();
    void endInteraction();

    void animateLeftBehindView(float value);

    boolean isInteractsWith(View view);
    // Returns the value identifying whether the specified velocity velocity is co-directional
    // with the offset direction.
    boolean isOpenable();
    boolean isFlyoutable();

    // Test an offset or velocity on applicability in current interaction.
    boolean isApplicable(float value);


    View getForeView();
    View getLeftBehindView();

    float getVelocityFrom(VelocityTracker tracker);

    float selectValue(float x, float y);
    boolean isInteractionStarted(float dx, float dy, float absDx, float absDy);
    DynamicAnimation.ViewProperty getAnimatedProperty();
    boolean isPointInForeView(float x, float y);

    void layout(int l, int t, int r, int b);
}
