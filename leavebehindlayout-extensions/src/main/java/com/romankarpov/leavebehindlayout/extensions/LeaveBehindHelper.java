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
            public void onStateChanged(int stateFlag) {
                mListener.onStateChanged(ViewHolder.this, stateFlag);
            }
        }

        interface Listener {
            void onStateChanged(ViewHolder holder, int stateFlag);
        }
    }
}

