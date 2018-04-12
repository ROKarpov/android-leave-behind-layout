package com.romankarpov.leavebehindlayout;

import android.support.animation.DynamicAnimation;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

class ClosedLayoutState implements LeaveBehindLayoutState {
    @Override
    public void applyLayout(LeaveBehindLayout layout) {
        InteractionModel config = layout.getActualInteractionModel();
        config.applyOffset(config.getClosedPosition());
        layout.invalidate();
    }

    @Override
    public boolean handleTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return handleDown(layout, event);
            case MotionEvent.ACTION_MOVE:
                return handleMove(layout, event);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Reset this flag for next time.
                Log.d("Closed State", "Handle Up");
                layout.endTrackEvent();
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean shouldInterceptTouchEvent(LeaveBehindLayout layout, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return shouldInterceptDown(layout, event);
            case MotionEvent.ACTION_MOVE:
                return handleMove(layout, event);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d("Closed State", "Should Intercept Up");
                layout.endTrackEvent();
                return true;
            default:
                return false;
        }
    }

    boolean shouldInterceptDown(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Closed State", "Should Intercept Down");
        final InteractionModel model = layout.getActualInteractionModel();
        final int actionIndex = event.getActionIndex();
        final float x = event.getX(actionIndex);
        final float y = event.getY(actionIndex);

        final boolean shouldTrackEvent = model.isPointInForeView(x, y);
        if (shouldTrackEvent) {
            layout.startTrackEvent(actionIndex, x, y);
        }

        return shouldTrackEvent;
    }

    boolean handleDown(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Closed State", "Handle Down");
        final int actionIndex = layout.getActionIndex();
        if (event.getActionIndex() != actionIndex) return false;

        return layout.isEventTracked();
    }

    boolean handleMove(LeaveBehindLayout layout, MotionEvent event) {
        Log.d("Closed State", "Handle Move");
        final InteractionModel model = layout.getActualInteractionModel();
        final int actionIndex = layout.getActionIndex();
        if (event.getActionIndex() != actionIndex) return false;

        final int motionId = event.getPointerId(actionIndex);
        final float x = event.getX(motionId);
        final float y = event.getY(motionId);

        final float dx = x - layout.getMotionInitialX();
        final float dy = y - layout.getMotionInitialY();

        Log.d ("ClosedLayout", String.format("%f %f", dx, dy));
        if (canSelectOpeningParameters(dx, dy, layout.getTouchSlop())) {
            if (layout.setActualInteractionModelByOffset(dx, dy)) {
                layout.getParent().requestDisallowInterceptTouchEvent(true);
                layout.setState(LeaveBehindLayout.DRAGGING_STATE);
                layout.updateLastPosition(x, y);
                //layout.notifyInteractionStarted(config.getLeftBehindGravity(), config.getLeftBehindView());
                return true;
            }
        }

        return false;
    }

    @Override
    public float getFinalPositionFrom(InteractionModel model) {
        return model.getClosedPosition();
    }

    @Override
    public DynamicAnimation.OnAnimationUpdateListener getAnimationUpdateListener(LeaveBehindLayout layout) {
        return new AnimationUpdateListener(layout);
    }

    @Override
    public DynamicAnimation.OnAnimationEndListener getAnimationEndListener(LeaveBehindLayout layout) {
        return new AnimationEndListener(layout);
    }

    boolean canSelectOpeningParameters(float dx, float dy, float touchSlop) {
        return (dx > touchSlop) || (dx < -touchSlop) || (dy > touchSlop) || (dy < -touchSlop);
    }

    @Override
    public void onStateSpecified(LeaveBehindLayout layout) {
        // TODO: IMPLEMENT!
    }

    class AnimationUpdateListener implements DynamicAnimation.OnAnimationUpdateListener {
        LeaveBehindLayout mLayout;

        public AnimationUpdateListener(LeaveBehindLayout layout) {
            mLayout = layout;
        }

        @Override
        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
            final InteractionModel model = mLayout.getActualInteractionModel();
            model.animateLeftBehindView(value);
            final int gravity = model.getGravity();
            final View view = model.getLeftBehindView();
            final float progress = model.getOpeningProgress();
            mLayout.dispatchLeaveBehindOpeningProgress(gravity, view, progress);
        }
    }

    class AnimationEndListener implements DynamicAnimation.OnAnimationEndListener {
        private LeaveBehindLayout mLayout;

        public AnimationEndListener(LeaveBehindLayout layout) {
            mLayout = layout;
        }
        @Override
        public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
            final InteractionModel model = mLayout.getActualInteractionModel();
            mLayout.dispatchLeaveBehindClosed(model.getGravity(), model.getLeftBehindView());
        }
    }
}
