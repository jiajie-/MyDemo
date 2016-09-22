package com.jiajie.design.preview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jiajie.design.R;
import com.jiajie.design.widgets.ZoomImageView;

/**
 * PreviewActivity2
 * Created by jiajie on 16/9/18.
 */
public class PreviewActivity2 extends AppCompatActivity {

    private static final String TAG = "PreviewActivity2";

    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        final String[] urls = getIntent().getStringArrayExtra("urls");
        final int index = getIntent().getIntExtra("index", 0);

        final ImageView[] mImageViews = new ImageView[urls.length];

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView imageView = new ZoomImageView(container.getContext());
                Log.e(TAG, "instantiateItem: " + urls[position]);
                Glide.with(container.getContext())
                        .load(urls[position])
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
                container.addView(imageView);
                mImageViews[position] = imageView;
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position]);
            }

            @Override
            public int getCount() {
                return mImageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return object == view;
            }
        });

        mViewPager.setCurrentItem(index);

    }
}
