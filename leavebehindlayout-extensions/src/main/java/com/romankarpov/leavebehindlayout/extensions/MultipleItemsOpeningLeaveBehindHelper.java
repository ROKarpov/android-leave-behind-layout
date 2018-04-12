package com.romankarpov.leavebehindlayout.extensions;

import java.util.HashMap;
import java.util.Map;
import com.romankarpov.leavebehindlayout.LeaveBehindLayout;

class MultipleItemsOpeningLeaveBehindHelper
        implements LeaveBehindHelper, LeaveBehindHelper.ViewHolder.Listener {
    final Map<Integer, Integer> mOpenedItemPositions = new HashMap<>(1);

    @Override
    public void registerViewHolder(ViewHolder viewHolder) {
        viewHolder.onCreateViewHolder();
        viewHolder.setListener(this);
    }

    @Override
    public void restoreViewHolderState(ViewHolder viewHolder) {
        final LeaveBehindLayout layout = viewHolder.getLeaveBehindLayout();
        final int position = viewHolder.getAdapterPosition();
        if (mOpenedItemPositions.containsKey(position)) {
            layout.openLeftBehind(mOpenedItemPositions.get(position), false);
        }
        else {
            layout.closeLeftBehind(false);
        }
    }

    @Override
    public void onLeaveBehindClosed(ViewHolder holder, int gravity) {
        final int currentPosition = holder.getAdapterPosition();
        if (mOpenedItemPositions.containsKey(currentPosition)) {
            mOpenedItemPositions.remove(currentPosition);
        }
    }

    @Override
    public void onLeaveBehindOpened(ViewHolder holder, int gravity) {
        final int currentPosition = holder.getAdapterPosition();
        mOpenedItemPositions.put(
                currentPosition,
                holder.getLeaveBehindLayout().getOpenedLeftBehindGravity());
    }

    @Override
    public void onLeaveBehindOpening(ViewHolder holder, int gravity, float progress) {

    }
}
