package com.jiajie.design.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiajie.design.R;
import com.jiajie.design.widgets.radar.RadarItem;
import com.jiajie.design.widgets.radar.RadarView;

import java.util.ArrayList;
import java.util.List;

/**
 * RadarFragment for radar view.
 * Created by jiajie on 16/9/9.
 */
public class RadarFragment extends Fragment {

    private static final String TAG = "RadarFragment";

    RadarView radarView;

    List<RadarItem> list = new ArrayList<>();

    String json = "[\n" +
            "{\"id\":1,\"name\":\"nanuto\",\"list\":[{\"name\":\"幻\",\"value\":10},{\"name\":\"贤\",\"value\":10},{\"name\":\"力\",\"value\":10},{\"name\":\"速\",\"value\":10},{\"name\":\"精\",\"value\":10},{\"name\":\"印\",\"value\":10},{\"name\":\"忍\",\"value\":10},{\"name\":\"体\",\"value\":10}]},\n" +
            "{\"id\":2,\"name\":\"tiantian\",\"list\":[{\"name\":\"幻\",\"value\":10},{\"name\":\"贤\",\"value\":10},{\"name\":\"力\",\"value\":10},{\"name\":\"速\",\"value\":10},{\"name\":\"精\",\"value\":10},{\"name\":\"印\",\"value\":10},{\"name\":\"忍\",\"value\":10},{\"name\":\"体\",\"value\":10}]},\n" +
            "{\"id\":3,\"name\":\"lee\",\"list\":[{\"name\":\"幻\",\"value\":10},{\"name\":\"贤\",\"value\":10},{\"name\":\"力\",\"value\":10},{\"name\":\"速\",\"value\":10},{\"name\":\"精\",\"value\":10},{\"name\":\"印\",\"value\":10},{\"name\":\"忍\",\"value\":10},{\"name\":\"体\",\"value\":10}]},\n" +
            "{\"id\":4,\"name\":\"sasikei\",\"list\":[{\"name\":\"幻\",\"value\":10},{\"name\":\"贤\",\"value\":10},{\"name\":\"力\",\"value\":10},{\"name\":\"速\",\"value\":10},{\"name\":\"精\",\"value\":10},{\"name\":\"印\",\"value\":10},{\"name\":\"忍\",\"value\":10},{\"name\":\"体\",\"value\":10}]}\n" +
            "]";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_radar, container, false);

        radarView = (RadarView) rootView.findViewById(R.id.radar);

        getNanuto();
        if (radarView != null) {
            radarView.setRadarItem(list.get(2));
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }



    private void getNanuto() {
        list = new Gson().fromJson(json, new TypeToken<List<RadarItem>>() {
        }.getType());
        Log.e(TAG, "getNanuto: " + list.size());
    }

}
