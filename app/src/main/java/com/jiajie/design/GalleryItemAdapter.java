package com.jiajie.design;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiajie.design.GalleryFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a item and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GalleryItemAdapter extends RecyclerView.Adapter<GalleryItemAdapter.ViewHolder> {

    private static final String TAG = GalleryItemAdapter.class.getSimpleName();

    private Context mContext;
    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public GalleryItemAdapter(Context context, List<String> items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mImageUrl = mValues.get(position);

        Glide.with(mContext)
                .load(mValues.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .skipMemoryCache(true) //disable RAM cache,but still use DISK cache.
//                .asGif() //for gif,but speed too slow ...
                .placeholder(R.drawable.ic_place_holder) // can also be a drawable.
                .error(R.drawable.ic_menu_exit) // will be displayed if the image cannot be loaded.
//                .crossFade(300)
//                .priority(Priority.NORMAL) // set priority of this picture
//                .dontAnimate() //directly show image without animate.
//                .override(100,100)
//                .centerCrop()
//                .fitCenter()
//                .thumbnail(0.1f)
                .into((ImageView) holder.mView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mImageUrl);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public String mImageUrl;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mImageUrl + "'";
        }
    }
}
