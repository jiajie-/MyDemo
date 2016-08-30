package com.jiajie.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiajie.design.widgets.speed.FirstSpeedView;

/**
 * for my custom view display
 * Created by jiajie on 16/8/11.
 */
public class MyViewActivity extends AppCompatActivity {

    private FirstSpeedView firstSpeedView;
    SeekBar seekBar;
    TextView textSpeed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view);

        firstSpeedView = (FirstSpeedView) findViewById(R.id.speed_view);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textSpeed = (TextView) findViewById(R.id.textSpeed);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSpeed.setText(String.format("%d", progress));
                firstSpeedView.speedTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
