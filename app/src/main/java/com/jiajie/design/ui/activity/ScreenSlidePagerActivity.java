package com.jiajie.design.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jiajie.design.R;
import com.jiajie.design.api.DataResult;
import com.jiajie.design.preview.PreviewActivity2;
import com.jiajie.design.ui.fragment.BannerFragment;
import com.jiajie.design.ui.fragment.BezierFragment;
import com.jiajie.design.ui.fragment.GalleryFragment;
import com.jiajie.design.ui.fragment.RadarFragment;
import com.jiajie.design.ui.fragment.SelectPhotoFragment;
import com.jiajie.design.ui.fragment.SpeedViewFragment;

import java.util.List;

/**
 * ScreenSlidePagerActivity
 * Created by jiajie on 16/9/9.
 */
public class ScreenSlidePagerActivity extends AppCompatActivity implements GalleryFragment.OnListFragmentInteractionListener {

    private static final String TAG = ScreenSlidePagerActivity.class.getSimpleName();

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

//        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mPager.setPageTransformer(true, new DepthPageTransformer());

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onListFragmentInteraction(List<DataResult> list, DataResult item) {
        int index = list.indexOf(item);
        int size = list.size();

        String[] urls = new String[list.size()];
        Intent intent = new Intent(this, PreviewActivity2.class);
        for (int i = 0; i < size; i++) {
            urls[i] = list.get(i).getUrl();
        }
        intent.putExtra("urls", urls);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 6;

        private static final int FRAGMENT_SELECT_PHOTO = 0;
        private static final int FRAGMENT_GALLERY = 1;
        private static final int FRAGMENT_BANNER = 2;
        private static final int FRAGMENT_SPEED = 3;
        private static final int FRAGMENT_RADAR = 4;
        private static final int FRAGMENT_BEZIER = 5;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "getItem: " + position);
            switch (position) {
                case FRAGMENT_SELECT_PHOTO:
                    return new SelectPhotoFragment();
                case FRAGMENT_GALLERY:
                    return new GalleryFragment();
                case FRAGMENT_SPEED:
                    return new SpeedViewFragment();
                case FRAGMENT_RADAR:
                    return new RadarFragment();
                case FRAGMENT_BEZIER:
                    return new BezierFragment();
                case FRAGMENT_BANNER:
                    return new BannerFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }


}
