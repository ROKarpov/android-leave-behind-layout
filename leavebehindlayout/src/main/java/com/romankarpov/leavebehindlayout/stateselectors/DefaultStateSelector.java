package com.romankarpov.leavebehindlayout.stateselectors;

import com.romankarpov.leavebehindlayout.LeaveBehindLayout;
import com.romankarpov.leavebehindlayout.LeaveBehindLayoutState;
import com.romankarpov.leavebehindlayout.core.InteractionModel;

public class DefaultStateSelector implements StateSelector {
    final static float PROGRESS_THRESHOLD = 0.5f;

    private float mMinVelocity;
    private float mMaxVelocity;

    public DefaultStateSelector(int minVelocity, int maxVelocity) {
        if (minVelocity >= maxVelocity)
            throw new IllegalArgumentException("The 'minVelocity' must be larger than 'maxVelocity'");
        mMinVelocity = minVelocity;
        mMaxVelocity = maxVelocity;
    }

    @Override
    public LeaveBehindLayoutState selectState(InteractionModel model, float velocity) {
        float normalizedVelocity = normalizeVelocity(velocity);
        boolean velocityCodirectionaledWithOffset = model.isApplicable(normalizedVelocity);
        if (velocityCodirectionaledWithOffset) {
            if ((normalizedVelocity != 0) && model.isFlyoutable()) {
                return LeaveBehindLayout.FLYOUT_STATE;
            } else {
                final float progress = model.getOpeningProgress();
                if (progress >= PROGRESS_THRESHOLD) {
                    return LeaveBehindLayout.OPENED_STATE;
                } else {
                    return LeaveBehindLayout.CLOSED_STATE;
                }
            }
        } else {
            return LeaveBehindLayout.CLOSED_STATE;
        }
    }

    private float normalizeVelocity(float v) {
        if ((v < mMinVelocity) && (v > -mMinVelocity)) {
            return 0.f;
        } else if ( v > 0) {
            return Math.min(v, mMaxVelocity);
        } else {
            return Math.max(v, -mMaxVelocity);
        }
    }
}
