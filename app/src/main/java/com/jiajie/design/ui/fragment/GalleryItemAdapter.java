package com.jiajie.design.ui.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiajie.design.R;
import com.jiajie.design.ui.fragment.GalleryFragment.OnListFragmentInteractionListener;
import com.jiajie.design.api.DataResult;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a item and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class GalleryItemAdapter extends RecyclerView.Adapter<GalleryItemAdapter.ViewHolder> {

    private static final String TAG = GalleryItemAdapter.class.getSimpleName();

    private Context mContext;
    private final List<DataResult> mValues;
    private final OnListFragmentInteractionListener mListener;

    public GalleryItemAdapter(Context context, List<DataResult> items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_girl, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.text = (TextView) view.findViewById(R.id.desc);
        holder.image = (ImageView) view.findViewById(R.id.image);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DataResult result = mValues.get(position);

        Glide.with(mContext)
                .load(result.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .skipMemoryCache(true) //disable RAM cache,but still use DISK cache.
//                .asGif() //for gif,but speed too slow ...
                .placeholder(R.drawable.ic_place_holder) // can also be a drawable.
                .error(R.drawable.ic_place_holder) // will be displayed if the image cannot be loaded.
//                .crossFade(300)
//                .priority(Priority.NORMAL) // set priority of this picture
//                .dontAnimate() //directly show image without animate.
//                .override(200, 200)
//                .centerCrop()
//                .fitCenter()
//                .thumbnail(0.1f)
                .into(holder.image);

        holder.text.setText(result.getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(mValues,result);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
