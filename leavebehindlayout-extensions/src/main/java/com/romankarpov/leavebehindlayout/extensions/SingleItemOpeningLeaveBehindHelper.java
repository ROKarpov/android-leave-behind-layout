package com.romankarpov.leavebehindlayout.extensions;

import android.view.Gravity;
import com.romankarpov.leavebehindlayout.LeaveBehindLayout;

class SingleItemOpeningLeaveBehindHelper implements LeaveBehindHelper, LeaveBehindHelper.ViewHolder.Listener {
    private final static int EMPTY_OPENED_ITEM_POSITION = -1;
    private final static int EMPTY_GRAVITY = Gravity.NO_GRAVITY;

    private int mOpenedItemPosition = EMPTY_OPENED_ITEM_POSITION;
    private ViewHolder mOpenedItem = null;
    private int mOpenedGravity = EMPTY_GRAVITY;

    @Override
    public void registerViewHolder(ViewHolder viewHolder) {
        viewHolder.onCreateViewHolder();
        viewHolder.setListener(this);
    }

    @Override
    public void restoreViewHolderState(ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() == mOpenedItemPosition) {
            viewHolder.getLeaveBehindLayout().openLeftBehind(mOpenedGravity, false);
        } else {
            viewHolder.getLeaveBehindLayout().closeLeftBehind(false);
        }
    }

    @Override
    public void onLeaveBehindClosed(ViewHolder holder, int gravity) {
        if (holder.getAdapterPosition() == mOpenedItemPosition) {
            mOpenedItem = null;
            mOpenedItemPosition = EMPTY_OPENED_ITEM_POSITION;
            mOpenedGravity = EMPTY_GRAVITY;
        }
    }

    @Override
    public void onLeaveBehindOpened(ViewHolder holder, int gravity) {
        mOpenedItem = holder;
        mOpenedItemPosition = holder.getAdapterPosition();
        mOpenedGravity = holder.getLeaveBehindLayout().getOpenedLeftBehindGravity();
    }

    @Override
    public void onLeaveBehindOpening(ViewHolder holder, int gravity, float progress) {
        if ((mOpenedItemPosition != EMPTY_OPENED_ITEM_POSITION)
                && (mOpenedItemPosition != holder.getAdapterPosition())
                && (mOpenedItem.getAdapterPosition() == mOpenedItemPosition)) {
            mOpenedItem.getLeaveBehindLayout().closeLeftBehind();
        }
        mOpenedItem = null;
        mOpenedItemPosition = EMPTY_OPENED_ITEM_POSITION;
        mOpenedGravity = EMPTY_GRAVITY;
    }
}
