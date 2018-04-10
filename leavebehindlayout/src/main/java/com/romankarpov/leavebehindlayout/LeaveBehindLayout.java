package com.romankarpov.leavebehindlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import com.romankarpov.leavebehindlayout.core.InteractionModel;
import com.romankarpov.leavebehindlayout.core.InteractionModelsBuilder;
import com.romankarpov.leavebehindlayout.animations.LeftBehindViewAnimation;
import com.romankarpov.leavebehindlayout.stateselectors.DefaultStateSelector;
import com.romankarpov.leavebehindlayout.stateselectors.StateSelector;

import org.jetbrains.annotations.NotNull;

public class LeaveBehindLayout extends ViewGroup {
    private static int DEFAULT_FLYOUTABLE_SIDES =
            Gravity.START
            |Gravity.LEFT
            | Gravity.TOP
            | Gravity.RIGHT
            | Gravity.END
            | Gravity.BOTTOM;

    public static final int FLAG_CLOSED = 0;
    public static final LeaveBehindLayoutState CLOSED_STATE = new ClosedLayoutState();
    public static final int FLAG_DRAGGING = 1;
    public static final LeaveBehindLayoutState DRAGGING_STATE = new DraggingLayoutState();
    public static final int FLAG_OPENED = 2;
    public static final LeaveBehindLayoutState OPENED_STATE = new OpenedLayoutState();
    public static final int FLAG_FLYOUT = 4;
    public static final LeaveBehindLayoutState FLYOUT_STATE = new FlyoutLayoutState();


    InteractionModel[] mInteractionModels;
    InteractionModel mActualInteractionModel;

    private StateSelector mStateSelector;
    private LeaveBehindLayoutState mState;
    private LeftBehindViewAnimation mLeftBehindViewAnimation;

    private VelocityTracker mVelocityTracker;
    private DynamicAnimation mAnimation;

    private int mFlyoutableFlags;
    private final List<Listener> mListeners = new ArrayList<>(1);

    private boolean mIsInterceptingTouchEvent;
    private int mActionIndex;
    private float mMotionInitialX;
    private float mMotionInitialY;
    private float mMotionLastX;
    private float mMotionLastY;

    private int mTouchSlop;
    // It here for memory optimization.
    private final List<View> mMatchParentChildren = new ArrayList<>(1);

    public LeaveBehindLayout(Context context) {
        this(context, null);
    }
    public LeaveBehindLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public LeaveBehindLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LeaveBehindLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    //region Overriding ViewGroup and View methods.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        LayoutParams params = new LayoutParams(getContext(), attrs);
        return params;
    }
    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return p;
        }
        else if (p instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams)p);
        }
        else {
            return new LayoutParams(p);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mState.shouldInterceptTouchEvent(this, ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mState.handleTouchEvent(this, event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            final int layoutLeft = getPaddingLeft();
            final int layoutTop = getPaddingTop();
            final int layoutRight = r - l - getPaddingRight();
            final int layoutBottom = b - t - getPaddingBottom();

            for (int i = 0; i < mInteractionModels.length; ++i) {
                mInteractionModels[i].layout(layoutLeft, layoutTop, layoutRight, layoutBottom);
                mInteractionModels[i].endInteraction();
            }
            bringChildToFront(mActualInteractionModel.getForeView());
            mActualInteractionModel.startInteraction();
            mState.applyLayout(this);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        final boolean measureMatchParentChildren =
                MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                        MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if (lp.width == FrameLayout.LayoutParams.MATCH_PARENT ||
                            lp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));

        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final View child = mMatchParentChildren.get(i);
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                final int childWidthMeasureSpec;
                if (lp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    final int width = Math.max(0, getMeasuredWidth()
                            - getPaddingLeft() - getPaddingRight()
                            - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight() +
                                    lp.leftMargin + lp.rightMargin,
                            lp.width);
                }

                final int childHeightMeasureSpec;
                if (lp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getMeasuredHeight()
                            - getPaddingTop() - getPaddingBottom()
                            - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom() +
                                    lp.topMargin + lp.bottomMargin,
                            lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

        final int layoutDirection = ViewCompat.getLayoutDirection(this);

        count = getChildCount();
        InteractionModelsBuilder builder = new InteractionModelsBuilder()
                .forLayout(this);
        for(int i = 0; i < count; ++i) {
            builder.fillFromView(getChildAt(i), layoutDirection);
        }
        final int flyoutableFlags = getFlyoutableFlags();
        builder.fillFlyoutableIfExists(flyoutableFlags, Gravity.TOP, layoutDirection)
                .fillFlyoutableIfExists(flyoutableFlags, Gravity.BOTTOM, layoutDirection)
                .fillFlyoutableIfExists(flyoutableFlags, Gravity.START, layoutDirection)
                .fillFlyoutableIfExists(flyoutableFlags, Gravity.END, layoutDirection);
        mInteractionModels = builder.build();
        if (mInteractionModels.length > 0) {
            mActualInteractionModel = mInteractionModels[0];
        }
    }
    //endregion

    //region Layout-specific methods.
    public void openLeftBehind(int gravity) {
        openLeftBehind(gravity, true);
    }
    public void openLeftBehind(View view) {
        openLeftBehind(view, true);
    }
    public void openLeftBehind(int gravity, boolean isAnimated) {
        if (setActualInteractionModelByGravity(gravity)) {
            setState(LeaveBehindLayout.OPENED_STATE);
            if (isAnimated) {
                startAnimationToCurrentState();
            } else {
                mState.applyLayout(this);
            }
        }
    }
    public void openLeftBehind(View view, boolean isAnimated) {
        if (setActualInteractionModelByView(view)) {
            setState(LeaveBehindLayout.OPENED_STATE);
            if (isAnimated) {
                startAnimationToCurrentState();
            } else {
                mState.applyLayout(this);
            }
        }
    }
    public void closeLeftBehind() {
        closeLeftBehind(true);
    }
    public void closeLeftBehind(boolean isAnimated) {
        setState(LeaveBehindLayout.CLOSED_STATE);
        if (isAnimated) {
            startAnimationToCurrentState();
        } else {
            invalidate();
        }
    }

    public LeftBehindViewAnimation getLeftBehindViewAnimation() {
        return mLeftBehindViewAnimation;
    }
    public LeaveBehindLayout setLeftBehindViewAnimation(@NotNull LeftBehindViewAnimation leftBehindViewAnimation) {
        mLeftBehindViewAnimation = leftBehindViewAnimation;
        // TODO: IMPLEMENT ANIMATION RUNTIME UPDATE IN INTERACTION_MODELS.
        return this;
    }

    public int getOpenedLeftBehindGravity() {
        return mActualInteractionModel.getGravity();
    }

    public StateSelector getStateSelector() {
        return mStateSelector;
    }
    public LeaveBehindLayout setStateSelector(@NotNull StateSelector stateSelector) {
        mStateSelector = stateSelector;
        return this;
    }

    public int getFlyoutableFlags() {
        return mFlyoutableFlags;
    }
    public LeaveBehindLayout setFlyoutableFlags(int flyoutableFlags) {
        mFlyoutableFlags = flyoutableFlags;
        return this;
    }

    public InteractionModel getActualInteractionModel() {
        return mActualInteractionModel;
    }
    // FOR INTERNAL USE ONLY!
    public boolean setActualInteractionModel(InteractionModel actualInteractionModel) {
        mActualInteractionModel.endInteraction();
        mActualInteractionModel = actualInteractionModel;
        mActualInteractionModel.startInteraction();
        return true;
    }
    public boolean setActualInteractionModelByGravity(int gravity) {
        for (int i = 0; i < mInteractionModels.length; ++i) {
            if (mInteractionModels[i].getGravity() == gravity) {
                return setActualInteractionModel(mInteractionModels[i]);
            }
        }
        return false;
    }
    public boolean setActualInteractionModelByView(View view) {
        for (int i = 0; i < mInteractionModels.length; ++i) {
            if (mInteractionModels[i].getLeftBehindView() == view) {
                return setActualInteractionModel(mInteractionModels[i]);
            }
        }
        return false;
    }
    public boolean setActualInteractionModelByOffset(float dx, float dy) {
        final float absDx = Math.abs(dx);
        final float absDy = Math.abs(dy);

        for (int i = 0; i < mInteractionModels.length; ++i) {
            if (mInteractionModels[i].isInteractionStarted(dx, dy, absDx, absDy)) {
                return setActualInteractionModel(mInteractionModels[i]);
            }
        }
        return false;
    }

    LeaveBehindLayoutState getState() {
        return mState;
    }
    void setState(LeaveBehindLayoutState state) {
        if ((state == null) || (mState == state)) return;
        mState = state;
        dispatchStateChanged(mState.getFlag());
    }

    void startAnimationToCurrentState() {
        endAnimation();
        float finalPosition = mState.getFinalPositionFrom(mActualInteractionModel);

        float startVelocoty = mVelocityTracker != null
                ? mActualInteractionModel.getVelocityFrom(mVelocityTracker)
                : 0f;
        DynamicAnimation.ViewProperty animatedProperty = mActualInteractionModel.getAnimatedProperty();

        final SpringForce force = new SpringForce();
        force.setFinalPosition(finalPosition)
                .setStiffness(SpringForce.STIFFNESS_MEDIUM)
                .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        final SpringAnimation animation = new SpringAnimation(mActualInteractionModel.getForeView(), animatedProperty);
        animation.setSpring(force);
        animation.setStartVelocity(startVelocoty);
        animation.addUpdateListener(mState.getAnimationUpdateListener(this));
        animation.addEndListener(mState.getAnimationEndListener(this));
        mAnimation = animation;
        mAnimation.start();
    }
    void endAnimation() {
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    VelocityTracker getVelocityTracker() {
        return mVelocityTracker;
    }

    public boolean isEventTracked() {
        return mIsInterceptingTouchEvent;
    }
    public void startTrackEvent(int actionIndex, float x, float y) {
        if (!mIsInterceptingTouchEvent) {
            mIsInterceptingTouchEvent = true;
            mActionIndex = actionIndex;
            mMotionInitialX = x;
            mMotionInitialY = y;
            mMotionLastX = x;
            mMotionLastY = y;

            mVelocityTracker = VelocityTracker.obtain();
        }
    }
    void endTrackEvent() {
        mIsInterceptingTouchEvent = false;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    void updateLastPosition(float x, float y) {
        mMotionLastX = x;
        mMotionLastY = y;
    }
    int getActionIndex() {
        return mActionIndex;
    }
    float getMotionInitialX() {
        return mMotionInitialX;
    }
    float getMotionInitialY() {
        return mMotionInitialY;
    }
    float getMotionLastX() {
        return mMotionLastX;
    }
    float getMotionLastY() {
        return mMotionLastY;
    }

    int getTouchSlop() { return mTouchSlop; }
    //endregion

    //region listeners API
    public void addListener(Listener listener) {
        mListeners.add(listener);
    }
    public boolean removeListener(Listener listener) {
        return mListeners.remove(listener);
    }
    public void removeAllListeners() {
        mListeners.clear();
    }

    void dispatchLeaveBehindOpeningProgress(int gravity, View view, float progress) {
        for (Listener listener : mListeners) {
            listener.onLeaveBehindOpeningProgress(gravity, view, progress);
        }
    }
    void dispatchLeaveBehindClosed(int gravity, View view) {
        for (Listener listener : mListeners) {
            listener.onLeaveBehindClosed(gravity, view);
        }
    }
    void dispatchLeaveBehindOpened(int gravity, View view) {
        for (Listener listener : mListeners) {
            listener.onLeaveBehindOpened(gravity, view);
        }
    }
    void dispatchFlyingOut(int gravity, float progress) {
        for (Listener listener : mListeners) {
            listener.onFlyingOutProgress(gravity, progress);
        }
    }
    void dispatchFlewOut(int gravity) {
        for (Listener listener : mListeners) {
            listener.onFlewOut(gravity);
        }
    }
    void dispatchStateChanged(int stateFlag) {
        for (Listener listener : mListeners) {
            listener.onStateChanged(stateFlag);
        }
    }
    //endregion

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        int maxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        int minFlingVelocity = vc.getScaledMinimumFlingVelocity();

        mState = LeaveBehindLayout.CLOSED_STATE;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LeaveBehindLayout,
                defStyleAttr, defStyleRes);
        mFlyoutableFlags = a.getInt(R.styleable.LeaveBehindLayout_flyoutableSides, DEFAULT_FLYOUTABLE_SIDES);

        final int animationFlag = a.getInt(
                R.styleable.LeaveBehindLayout_leftBehindViewAnimation,
                AnimationHelper.DEFAULT_ANIMATION);
        mLeftBehindViewAnimation = AnimationHelper.getAnimation(animationFlag);

        mStateSelector = new DefaultStateSelector(minFlingVelocity, maxFlingVelocity);

        a.recycle();
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static int DEFAULT_GRAVITY = Gravity.NO_GRAVITY;
        private static boolean DEFAULT_IS_OPENABLE = true;

        private static final int[] LAYOUT_ATTRS = {
                android.R.attr.layout_gravity,
                R.attr.layout_openable
        };

        public int gravity = DEFAULT_GRAVITY;
        public boolean isOpenable = DEFAULT_IS_OPENABLE;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
            gravity = a.getInt(R.styleable.LeaveBehindLayout_LayoutParams_layout_gravity, DEFAULT_GRAVITY);
            isOpenable = a.getBoolean(R.styleable.LeaveBehindLayout_LayoutParams_layout_openable, DEFAULT_IS_OPENABLE);
        }

        public LayoutParams(int width, int height) {
            this(width, height, DEFAULT_GRAVITY, DEFAULT_IS_OPENABLE);
        }

        public LayoutParams(int width, int height, int gravity) {
            this(width, height, gravity, DEFAULT_IS_OPENABLE);
        }

        public LayoutParams(int width, int height, boolean isOpenable) {
            this(width, height, DEFAULT_GRAVITY, isOpenable);
        }

        public LayoutParams(int width, int height, int gravity, boolean isOpenable) {
            super(width, height);
            gravity = gravity;
            isOpenable = isOpenable;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            gravity = DEFAULT_GRAVITY;
            isOpenable = DEFAULT_IS_OPENABLE;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            gravity = DEFAULT_GRAVITY;
            isOpenable = DEFAULT_IS_OPENABLE;
        }
    }

    public interface Listener {
        //void onInteractionStarted(int gravity, View view);

        void onStateChanged(int stateFlag);

        void onLeaveBehindClosed(int gravity, View view);

        void onLeaveBehindOpeningProgress(int gravity, View view, float progress);
        void onLeaveBehindOpened(int gravity, View view);

        void onFlyingOutProgress(int gravity, float progress);
        void onFlewOut(int gravity);
    }
}