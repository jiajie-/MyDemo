package com.jiajie.design.widgets.speed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiajie.design.R;

/**
 * for my custom view display
 * Created by jiajie on 16/8/11.
 */
public class SpeedActivity extends AppCompatActivity {

    ThirdSpeedView thirdSpeedView;
    SeekBar seekBar,seekBar2;
    TextView textSpeed,textSpeed2;
    CheckBox withTremble, withEffects;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        thirdSpeedView = (ThirdSpeedView) findViewById(R.id.speed_view);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        textSpeed = (TextView) findViewById(R.id.textSpeed);
        textSpeed2 = (TextView) findViewById(R.id.textSpeed2);
        withTremble = (CheckBox) findViewById(R.id.withTremble);
        withEffects = (CheckBox) findViewById(R.id.withEffects);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSpeed.setText(String.format("%d", progress));
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
                textSpeed2.setText(String.format("%d", progress));
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

    }

}
