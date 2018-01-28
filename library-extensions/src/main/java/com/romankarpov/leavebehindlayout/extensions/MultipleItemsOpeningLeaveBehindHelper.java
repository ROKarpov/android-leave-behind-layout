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
    public void onStateChanged(ViewHolder holder, int stateFlag) {
        final int currentPosition = holder.getAdapterPosition();
        switch (stateFlag) {
            case LeaveBehindLayout.FLAG_CLOSED:
                if (mOpenedItemPositions.containsKey(currentPosition)) {
                    mOpenedItemPositions.remove(currentPosition);
                }
                break;
            case LeaveBehindLayout.FLAG_OPENED:
                mOpenedItemPositions.put(
                        holder.getAdapterPosition(),
                        holder.getLeaveBehindLayout().getOpenedLeaveBehindGravity());
                break;
        }
    }
}
