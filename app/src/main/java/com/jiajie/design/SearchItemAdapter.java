package com.jiajie.design;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiajie.design.SearchFragment.SearchInteractionListener;
import com.jiajie.design.api.SearchResult;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SearchResult} and makes a call to the
 * specified {@link SearchInteractionListener}.
 */
public class SearchItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SearchItemAdapter.class.getSimpleName();
    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_ITEM = 1;

    private final List<SearchResult> mValues;
    private final SearchInteractionListener mListener;

    public SearchItemAdapter(List<SearchResult> items, SearchInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: viewType:" + viewType);
        View view;
        if (viewType == TYPE_EMPTY) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_empty, parent, false);
            return new EmptyViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_search, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.size() > 0) {
            return TYPE_ITEM;
        } else {
            return TYPE_EMPTY;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder: " + position);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).mItem = mValues.get(position);
//        ((ViewHolder)holder).mImage.setImageBitmap();
            ((ViewHolder) holder).mTitle.setText(mValues.get(position).getDesc());
            ((ViewHolder) holder).mAuthor.setText(mValues.get(position).getWho());
            ((ViewHolder) holder).mType.setText(mValues.get(position).getType());
            ((ViewHolder) holder).mTime.setText(mValues.get(position).getPublishedAt());

            ((ViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onSearchItemClick(((ViewHolder) holder).mItem);
                    }
                }
            });
        } else if (holder instanceof EmptyViewHolder) {
//            ((EmptyViewHolder)holder).mEmpty.setText();
        }
    }

    public void addItem(int position, SearchResult searchResult) {
        mValues.add(position, searchResult);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    public void clearItems() {
        mValues.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImage;
        public final TextView mTitle;
        public final TextView mAuthor;
        public final TextView mType;
        public final TextView mTime;
        public SearchResult mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImage = (ImageView) itemView.findViewById(R.id.iv_image);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            mType = (TextView) itemView.findViewById(R.id.tv_type);
            mTime = (TextView) itemView.findViewById(R.id.tv_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mEmpty;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mEmpty = (TextView) itemView.findViewById(R.id.tv_empty);
        }
    }

}
