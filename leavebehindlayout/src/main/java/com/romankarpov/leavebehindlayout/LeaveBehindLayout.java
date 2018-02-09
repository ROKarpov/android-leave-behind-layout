package com.romankarpov.leavebehindlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.animation.DynamicAnimation;
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

import com.romankarpov.leavebehindlayout.leavebehindviewanimations.LeftBehindViewAnimationProvider;
import com.romankarpov.leavebehindlayout.leavebehindviewanimations.StaticLeftBehindAnimationProvider;


public class LeaveBehindLayout extends ViewGroup {
    private static int DEFAULT_FLYOUTABLE_SIDES =
            Gravity.START
            |Gravity.LEFT
            | Gravity.TOP
            | Gravity.RIGHT
            | Gravity.END
            | Gravity.BOTTOM;
    private static LeftBehindViewAnimationProvider DEFAULT_ANIMATION_PROVIDER = StaticLeftBehindAnimationProvider.get();

    public static final int FLAG_CLOSED = 0;
    static final LeaveBehindLayoutState CLOSED_STATE = new ClosedLayoutState();
    public static final int FLAG_DRAGGING = 1;
    static final LeaveBehindLayoutState DRAGGING_STATE = new DraggingLayoutState();
    public static final int FLAG_OPENED = 2;
    static final LeaveBehindLayoutState OPENED_STATE = new OpenedLayoutState();
    public static final int FLAG_FLYOUT = 4;
    static final LeaveBehindLayoutState FLYOUT_STATE = new FlyoutLayoutState();

    private LeaveBehindLayoutConfig mConfig;
    private LeftBehindViewAnimationProvider mLeftBehindViewAnimationProvider = DEFAULT_ANIMATION_PROVIDER;

    private LeaveBehindLayoutState mState;
    private VelocityTracker mVelocityTracker;
//    /*private */PhysicsAnimator mPhysicsAnimator;
    private DynamicAnimation mAnimation;

    private int mFlyoutableFlags;
    private final List<Listener> mListeners = new ArrayList<>(1);

    private boolean mIsAnimated;
    private boolean mIsInterceptingTouchEvent;
    private int mActionIndex;
    private float mMotionInitialX;
    private float mMotionInitialY;
    private float mMotionLastX;
    private float mMotionLastY;
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
            mConfig.initialize(this, l, t, r, b);
            this.bringChildToFront(mConfig.getForeView());
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
        maxWidth += this.getPaddingLeft() + this.getPaddingRight();
        maxHeight += this.getPaddingTop() + this.getPaddingBottom();

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
                            - this.getPaddingLeft() - this.getPaddingRight()
                            - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            this.getPaddingLeft() + this.getPaddingRight() +
                                    lp.leftMargin + lp.rightMargin,
                            lp.width);
                }

                final int childHeightMeasureSpec;
                if (lp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getMeasuredHeight()
                            - this.getPaddingTop() - this.getPaddingBottom()
                            - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                            this.getPaddingTop() + this.getPaddingBottom() +
                                    lp.topMargin + lp.bottomMargin,
                            lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }
    //endregion

    //region Layout-specific methods.
    public void openLeftBehind(int gravity) {
        this.openLeftBehind(gravity, true);
    }
    public void openLeftBehind(View view) {
        this.openLeftBehind(view, true);
    }
    public void openLeftBehind(int gravity, boolean isAnimated) {
        if (mConfig.setOpeningParametersByGravity(gravity)) {
            this.setState(LeaveBehindLayout.OPENED_STATE);
            if (isAnimated) {
                this.startAnimationToCurrentState();
            } else {
                this.mState.applyLayout(this);
            }
        }
    }
    public void openLeftBehind(View view, boolean isAnimated) {
        if (mConfig.setOpeningParametersByView(view)) {
            this.setState(LeaveBehindLayout.OPENED_STATE);
            if (isAnimated) {
                this.startAnimationToCurrentState();
            } else {
                this.mState.applyLayout(this);
            }
        }
    }
    public void closeLeftBehind() {
        this.closeLeftBehind(true);
    }
    public void closeLeftBehind(boolean isAnimated) {
        this.setState(LeaveBehindLayout.CLOSED_STATE);
        if (isAnimated) {
            this.startAnimationToCurrentState();
        } else {
            this.mState.applyLayout(this);
        }
    }


    public int getOpenedLeaveBehindGravity() {
        return getConfig().getLeftBehindGravity();
    }

    public int getFlyoutableFlags() {
        return mFlyoutableFlags;
    }

    public void setFlyoutableFlags(int flyoutableFlags) {
        mFlyoutableFlags = flyoutableFlags;
    }

    public boolean isFlyoutable(int sideGravity) {
        return (mFlyoutableFlags & sideGravity) == sideGravity;
    }

    LeaveBehindLayoutConfig getConfig() {
        return mConfig;
    }
    VelocityTracker getVelocityTracker() {
        return mVelocityTracker;
    }

    public LeftBehindViewAnimationProvider getLeftBehindViewAnimationProvider() {
        return mLeftBehindViewAnimationProvider;
    }
    void startAnimationToCurrentState() {
        endAnimation();
        mAnimation =  mConfig.createAnimation(mState);
        mAnimation.start();
    }
    void endAnimation() {
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }
    }


    LeaveBehindLayoutState getState() {
        return mState;
    }
    void setState(LeaveBehindLayoutState state) {
        if ((state == null) || (mState == state)) return;
        mState = state;
        dispatchStateChanged(mState.getFlag());
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
        final int touchSlop = vc.getScaledTouchSlop();
        final int maxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        final int minFlingVelocity = vc.getScaledMinimumFlingVelocity();

        mConfig = new LeaveBehindLayoutConfig(touchSlop, minFlingVelocity, maxFlingVelocity);
        mState = LeaveBehindLayout.CLOSED_STATE;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LeaveBehindLayout,
                defStyleAttr, defStyleRes);
        mFlyoutableFlags = a.getInt(R.styleable.LeaveBehindLayout_flyoutableSides, DEFAULT_FLYOUTABLE_SIDES);
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
            this.gravity = gravity;
            this.isOpenable = isOpenable;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
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