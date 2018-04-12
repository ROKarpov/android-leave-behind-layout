package com.romankarpov.leavebehindlayout.extensions;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.romankarpov.leavebehindlayout.LeaveBehindLayout;
import com.romankarpov.leavebehindlayout.SimpleLeaveBehindLayoutListener;

public interface LeaveBehindHelper {
    void registerViewHolder(ViewHolder viewHolder);
    void restoreViewHolderState(ViewHolder viewHolder);

    class Builder {
        private boolean mIsMultipleItemsOpeningAllowed = false;

        public Builder withSingleItemOpeningAllowed() {
            mIsMultipleItemsOpeningAllowed = false;
            return this;
        }

        public Builder withMultipleItemsOpeningAllowed() {
            mIsMultipleItemsOpeningAllowed = true;
            return this;
        }

        public LeaveBehindHelper build() {
            if (mIsMultipleItemsOpeningAllowed) return new MultipleItemsOpeningLeaveBehindHelper();
            else return new SingleItemOpeningLeaveBehindHelper();
        }
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        private Listener mListener;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        abstract protected LeaveBehindLayout getLeaveBehindLayout();

        void onCreateViewHolder() {
            getLeaveBehindLayout().addListener(new LeaveBehindLayoutListener());
        }

        void setListener(Listener listener) {
            mListener = listener;
        }

        private class LeaveBehindLayoutListener extends SimpleLeaveBehindLayoutListener {
            @Override
            public void onLeaveBehindClosed(int gravity, View view) {
                mListener.onLeaveBehindClosed(ViewHolder.this, gravity);
            }

            @Override
            public void onLeaveBehindOpened(int gravity, View view) {
                mListener.onLeaveBehindOpened(ViewHolder.this, gravity);
            }

            @Override
            public void onLeaveBehindOpeningProgress(int gravity, View view, float progress) {
                mListener.onLeaveBehindOpening(ViewHolder.this, gravity, progress);
            }
        }

        interface Listener {
            void onLeaveBehindClosed(ViewHolder holder, int gravity);
            void onLeaveBehindOpened(ViewHolder holder, int gravity);
            void onLeaveBehindOpening(ViewHolder holder, int gravity, float progress);
        }
    }
}

