package com.jiajie.design.widgets.radar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiajie.design.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RadarActivity
 * Created by jiajie on 16/9/6.
 */
public class RadarActivity extends AppCompatActivity {

    private static final String TAG = "RadarActivity";

    RadarView radarView;

    List<RadarItem> list = new ArrayList<>();

    String json = "[\n" +
            "{\"id\":1,\"name\":\"nanuto\",\"list\":[{\"name\":\"幻\",\"value\":10},{\"name\":\"贤\",\"value\":10},{\"name\":\"力\",\"value\":10},{\"name\":\"速\",\"value\":10},{\"name\":\"精\",\"value\":10},{\"name\":\"印\",\"value\":10},{\"name\":\"忍\",\"value\":10},{\"name\":\"体\",\"value\":10}]},\n" +
            "{\"id\":2,\"name\":\"tiantian\",\"list\":[{\"name\":\"幻\",\"value\":10},{\"name\":\"贤\",\"value\":10},{\"name\":\"力\",\"value\":10},{\"name\":\"速\",\"value\":10},{\"name\":\"精\",\"value\":10},{\"name\":\"印\",\"value\":10},{\"name\":\"忍\",\"value\":10},{\"name\":\"体\",\"value\":10}]},\n" +
            "{\"id\":3,\"name\":\"lee\",\"list\":[{\"name\":\"幻\",\"value\":10},{\"name\":\"贤\",\"value\":10},{\"name\":\"力\",\"value\":10},{\"name\":\"速\",\"value\":10},{\"name\":\"精\",\"value\":10},{\"name\":\"印\",\"value\":10},{\"name\":\"忍\",\"value\":10},{\"name\":\"体\",\"value\":10}]},\n" +
            "{\"id\":4,\"name\":\"sasikei\",\"list\":[{\"name\":\"幻\",\"value\":10},{\"name\":\"贤\",\"value\":10},{\"name\":\"力\",\"value\":10},{\"name\":\"速\",\"value\":10},{\"name\":\"精\",\"value\":10},{\"name\":\"印\",\"value\":10},{\"name\":\"忍\",\"value\":10},{\"name\":\"体\",\"value\":10}]}\n" +
            "]";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        radarView = (RadarView) findViewById(R.id.radar);

        getNanuto();
        if (radarView != null) {
            radarView.setRadarItem(list.get(2));
        }

    }

    private void getNanuto() {
        list = new Gson().fromJson(json, new TypeToken<List<RadarItem>>() {
        }.getType());
        Log.e(TAG, "getNanuto: " + list.size());
    }

}
