package com.romankarpov.leavebehindlayout;

import com.romankarpov.leavebehindlayout.physics.PhysicsAnimationConfig;
import com.romankarpov.leavebehindlayout.physics.PhysicsObject;


abstract class AbstractAnimatorCallback implements PhysicsAnimationConfig.Callback {
        private LeaveBehindLayout mLayout;
        private LeaveBehindLayoutState mTerminalState;

        public AbstractAnimatorCallback(
                LeaveBehindLayout layout,
                LeaveBehindLayoutState terminalState
        ) {
            this.mLayout = layout;
            this.mTerminalState = terminalState;
        }

        protected LeaveBehindLayout getLayout() {
            return mLayout;
        }

        @Override
        public void onDoFrame(PhysicsAnimationConfig animationConfig) {
            final LeaveBehindLayoutConfig layoutConfig = mLayout.getConfig();

            final PhysicsObject physicsObject = animationConfig.getObject();
            final float offsetX = physicsObject.getPositionX();
            final float offsetY = physicsObject.getPositionY();

            layoutConfig.trySetOffset(offsetX, offsetY);
            notifyListenersOnProgress();
        }

        @Override
        public void onAnimationFinished(PhysicsAnimationConfig animationConfig) {
            mLayout.setState(mTerminalState);
            notifyListenersOnFinish();
        }

        protected abstract void notifyListenersOnFinish();

        protected abstract void notifyListenersOnProgress();
}
