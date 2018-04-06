package com.romankarpov.leavebehindlayout.core;


import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.View;

import com.romankarpov.leavebehindlayout.LeaveBehindLayout;
import com.romankarpov.leavebehindlayout.core.flyoutbehavior.FlyoutBehavior;
import com.romankarpov.leavebehindlayout.core.flyoutbehavior.FlyoutableBehavior;
import com.romankarpov.leavebehindlayout.core.flyoutbehavior.UnflyoutableBehavior;
import com.romankarpov.leavebehindlayout.animations.LeftBehindViewAnimation;
import com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors.EmptyViewBehavior;
import com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors.LeftBehindViewBehavior;
import com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors.OpenableViewBehavior;
import com.romankarpov.leavebehindlayout.core.leftbehindviewbehaviors.StaticViewBehavior;
import com.romankarpov.leavebehindlayout.core.unavailableoffsetbehavior.SwitchToOppositeOffsetBehavior;
import com.romankarpov.leavebehindlayout.core.unavailableoffsetbehavior.ZeroOffsetUnavailableOffsetBehavior;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class InteractionModelsBuilder {
    Map<Integer, InteractionModelConfig> mConfigs = new HashMap<>();
    LeaveBehindLayout mLayout;

    public InteractionModelConfig forAbsoluteGravity(int gravity) {
        Integer packedGravity = gravity;
        if (mConfigs.containsKey(packedGravity)) return mConfigs.get(packedGravity);
        InteractionModelConfig config = new InteractionModelConfig();
        mConfigs.put(packedGravity, config);
        return config;
    }
    public InteractionModelsBuilder forLayout(LeaveBehindLayout layout) {
        mLayout = layout;
        return this;
    }


    public InteractionModel[] build() {
        InteractionModelConfig config = mConfigs.get(Gravity.NO_GRAVITY);
        View foreView = config.mView;
        mConfigs.remove(Gravity.NO_GRAVITY);

        Map<Integer, AbstractInteractionModel> models = new HashMap<>(mConfigs.size());
        for (Map.Entry<Integer, InteractionModelConfig> item : mConfigs.entrySet()) {
            models.put(item.getKey(), createModelByConfig(item.getKey(), item.getValue(), foreView));
        }
        for (Map.Entry<Integer, AbstractInteractionModel> item : models.entrySet()) {
            Integer oppositeGravity = getOppositeGravity(item.getKey());
            item.getValue().setUnavailableOffsetBehavior(
                    models.containsKey(oppositeGravity)
                            ? new SwitchToOppositeOffsetBehavior(mLayout, models.get(oppositeGravity))
                            : new ZeroOffsetUnavailableOffsetBehavior());
        }
        InteractionModel[] result = new AbstractInteractionModel[models.size()];
        models.values().toArray(result);
        return result;
    }

    public InteractionModelsBuilder fillFromView(View view, int layoutDirection) {
        final LeaveBehindLayout.LayoutParams lp = (LeaveBehindLayout.LayoutParams) view.getLayoutParams();
        final int absGravity = GravityCompat.getAbsoluteGravity(lp.gravity, layoutDirection);

        forAbsoluteGravity(absGravity)
                .setView(view)
                .setGravity(lp.gravity)
                .setOpenable(lp.isOpenable);
        return this;
    }
    public InteractionModelsBuilder fillFlyoutableIfExists(int flyoutableFlags, int gravity, int layoutDirection) {
        if ((flyoutableFlags & gravity) == gravity) {
            final int absGravity = GravityCompat.getAbsoluteGravity(gravity, layoutDirection);
            forAbsoluteGravity(absGravity).setFlyoutable(true);
        }
        return this;
    }

    AbstractInteractionModel createModelByConfig(
            int gravity,
            InteractionModelConfig config,
            View foreView
    ) {
        AbstractInteractionModel model = null;
        switch (gravity) {
            case Gravity.TOP:
                model = new TopSideInteractionModel();
                break;
            case Gravity.BOTTOM:
                model = new BottomSideInteractionModel();
                break;
            case Gravity.LEFT:
                model = new LeftSideInteractionModel();
                break;
            case Gravity.RIGHT:
                model = new RightSideInteractionModel();
                break;
            default: throw new InvalidParameterException("Unsupported gravity");
        }

        LeftBehindViewBehavior leftBehindViewBehavior = null;
        if (config.mView != null) {
            if (config.mIsOpenable)
                leftBehindViewBehavior = new OpenableViewBehavior(config.mView, model);
            else
                leftBehindViewBehavior = new StaticViewBehavior(config.mView);
        }
        else
            leftBehindViewBehavior = new EmptyViewBehavior();

        FlyoutBehavior flyoutBehavior = (config.mIsFlyoutable)
                ? new FlyoutableBehavior()
                : new UnflyoutableBehavior();

        model.setForeView(foreView)
                .setGravity(config.mGravity)
                .setLeftBehindViewBehavior(leftBehindViewBehavior)
                .setFlyingOutBehavior(flyoutBehavior)
                .setAnimation(mLayout.getLeftBehindViewAnimation());
        return model;
    }

    private static Integer getOppositeGravity(Integer gravity) {
        switch (gravity) {
            case Gravity.LEFT: return Gravity.RIGHT;
            case Gravity.TOP: return Gravity.BOTTOM;
            case Gravity.RIGHT: return Gravity.LEFT;
            case Gravity.BOTTOM: return Gravity.TOP;
            default: throw new InvalidParameterException("Unsupported gravity");
        }
    }

    public class InteractionModelConfig {
        int mGravity;
        View mView;
        boolean mIsOpenable;
        boolean mIsFlyoutable;

        public InteractionModelConfig setGravity(int gravity) {
            mGravity = gravity;
            return this;
        }
        public InteractionModelConfig setView(View view) {
            mView = view;
            return this;
        }
        public InteractionModelConfig setOpenable(boolean openable) {
            mIsOpenable = openable;
            return this;
        }
        public InteractionModelConfig setFlyoutable(boolean flyoutable) {
            mIsFlyoutable = flyoutable;
            return this;
        }
    }
}
