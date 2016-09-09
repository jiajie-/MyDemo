package com.jiajie.design.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiajie.design.R;

/**
 * ScreenSlidePageFragment
 * Created by jiajie on 16/9/9.
 */
public class ScreenSlidePageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragement_screen_slide_page, container, false);
        return rootView;
    }
}
