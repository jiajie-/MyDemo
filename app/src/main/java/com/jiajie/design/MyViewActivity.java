package com.jiajie.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiajie.design.widgets.speed.SecondSpeedView;

/**
 * for my custom view display
 * Created by jiajie on 16/8/11.
 */
public class MyViewActivity extends AppCompatActivity {

//    private FirstSpeedView firstSpeedView;
//    SeekBar seekBar;
//    TextView textSpeed;

    SecondSpeedView secondSpeedView;
    SeekBar seekBar;
    TextView textSpeed;
    CheckBox withTremble, withEffects;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view);

//        firstSpeedView = (FirstSpeedView) findViewById(R.id.speed_view);
        secondSpeedView = (SecondSpeedView) findViewById(R.id.speed_view);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textSpeed = (TextView) findViewById(R.id.textSpeed);
        withTremble = (CheckBox) findViewById(R.id.withTremble);
        withEffects = (CheckBox) findViewById(R.id.withEffects);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSpeed.setText(String.format("%d", progress));
//                firstSpeedView.speedTo(seekBar.getProgress());
                secondSpeedView.speedTo(seekBar.getProgress());
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
                secondSpeedView.setWithTremble(isChecked);
            }
        });

        withEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                secondSpeedView.setWithEffects(isChecked);
            }
        });


    }

}
