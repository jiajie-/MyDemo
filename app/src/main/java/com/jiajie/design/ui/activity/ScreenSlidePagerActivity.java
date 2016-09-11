package com.jiajie.design.ui.activity;

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
import com.jiajie.design.ui.fragment.BezierFragment;
import com.jiajie.design.ui.fragment.RadarFragment;
import com.jiajie.design.ui.fragment.ScreenSlidePageFragment;
import com.jiajie.design.ui.fragment.SpeedViewFragment;
import com.jiajie.design.ui.fragment.YinYangFragment;
import com.jiajie.design.ui.transformer.ZoomOutPageTransformer;

/**
 * ScreenSlidePagerActivity
 * Created by jiajie on 16/9/9.
 */
public class ScreenSlidePagerActivity extends AppCompatActivity {

    private static final String TAG = ScreenSlidePagerActivity.class.getSimpleName();

    private static final int NUM_PAGES = 5;

    private static final int FRAGMENT_SPEED = 0;
    private static final int FRAGMENT_RADAR = 1;
    private static final int FRAGMENT_BEZIER = 2;
    private static final int FRAGMENT_YINYANG = 3;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "getItem: " + position);
            switch (position) {
                case FRAGMENT_SPEED:
                    return new SpeedViewFragment();
                case FRAGMENT_RADAR:
                    return new RadarFragment();
                case FRAGMENT_BEZIER:
                    return new BezierFragment();
                case FRAGMENT_YINYANG:
                    return new YinYangFragment();
                default:
                    return new ScreenSlidePageFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public int getItemPosition(Object object) {
            Log.i(TAG, "getItemPosition: ");
            return super.getItemPosition(object);
        }
    }


}
