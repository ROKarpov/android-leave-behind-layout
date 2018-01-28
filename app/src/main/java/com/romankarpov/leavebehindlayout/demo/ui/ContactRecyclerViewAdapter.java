package com.romankarpov.leavebehindlayout.demo.ui;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.romankarpov.leavebehindlayout.demo.R;
import com.romankarpov.leavebehindlayout.LeaveBehindLayout;
import com.romankarpov.leavebehindlayout.extensions.LeaveBehindHelper;
import com.romankarpov.leavebehindlayout.demo.model.Contact;


public class ContactRecyclerViewAdapter
        extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder> {

    private List<Contact> mValues;
    private final DiffCallback mDiffCallback = new DiffCallback();
    private final LeaveBehindHelper mLeaveBehindHelper = new LeaveBehindHelper.Builder()
            .withSingleItemOpeningAllowed()
            .build();

    public ContactRecyclerViewAdapter(List<Contact> items) {
        mValues = items;
    }

    public void setValues(final List<Contact> contacts) {
        mDiffCallback.initialize(mValues, contacts);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(mDiffCallback, true);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        mLeaveBehindHelper.registerViewHolder(holder);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Contact item = mValues.get(position);
        mLeaveBehindHelper.restoreViewHolderState(holder);
        holder.setName(item.getFullName());
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends LeaveBehindHelper.ViewHolder {
        private final LeaveBehindLayout mRootView;
        private final ImageView mAvatarImageView;
        private final TextView mNameTextView;

        public ViewHolder(View view) {
            super(view);
            mRootView = (LeaveBehindLayout)view;
            mAvatarImageView = (ImageView)view.findViewById(R.id.contact_list_item__image);
            mNameTextView = (TextView)view.findViewById(R.id.contact_list_item__name);
        }

        public void setAvatar(String image) {

        }

        public void setName(String name) {
            mNameTextView.setText(name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameTextView.getText() + "'";
        }

        @Override
        protected LeaveBehindLayout getLeaveBehindLayout() {
            return mRootView;
        }
    }

    class DiffCallback extends DiffUtil.Callback {
        List<Contact> mOldValues;
        List<Contact> mNewValues;

        public DiffCallback() {

        }

        public void initialize(List<Contact> oldValues, List<Contact> newValues) {
            mOldValues = oldValues;
            mNewValues = newValues;
        }

        @Override
        public int getOldListSize() {
            return mOldValues.size();
        }

        @Override
        public int getNewListSize() {
            return mNewValues.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Contact oldItem = mOldValues.get(oldItemPosition);
            Contact newItem = mOldValues.get(newItemPosition);
            return oldItem.getFullName() == newItem.getFullName();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Contact oldItem = mOldValues.get(oldItemPosition);
            Contact newItem = mOldValues.get(newItemPosition);
            return oldItem.getFullName().equals(newItem.getFullName());
        }
    }
}