package com.jiajie.design.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiajie.design.R;
import com.jiajie.design.widgets.speed.ThirdSpeedView;

import java.util.Locale;

/**
 * SpeedViewFragment for speed view.
 * Created by jiajie on 16/9/9.
 */
public class SpeedViewFragment extends Fragment {

    private static final String TAG = "SpeedViewFragment";

    ThirdSpeedView thirdSpeedView;
    SeekBar seekBar, seekBar2;
    TextView textSpeed, textSpeed2;
    CheckBox withTremble, withEffects;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_speed, container, false);

        thirdSpeedView = (ThirdSpeedView) rootView.findViewById(R.id.speed_view);
        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekBar2 = (SeekBar) rootView.findViewById(R.id.seekBar2);
        textSpeed = (TextView) rootView.findViewById(R.id.textSpeed);
        textSpeed2 = (TextView) rootView.findViewById(R.id.textSpeed2);
        withTremble = (CheckBox) rootView.findViewById(R.id.withTremble);
        withEffects = (CheckBox) rootView.findViewById(R.id.withEffects);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSpeed.setText(String.format(Locale.getDefault(), "%d", progress));
                thirdSpeedView.speedTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSpeed2.setText(String.format(Locale.getDefault(), "%d", progress));
                thirdSpeedView.rotateSpeedTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        withTremble.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thirdSpeedView.setWithTremble(isChecked);
            }
        });

        withEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thirdSpeedView.setWithEffects(isChecked);
            }
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }

}
