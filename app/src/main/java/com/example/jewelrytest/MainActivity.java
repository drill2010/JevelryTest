package com.example.jewelrytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener, View.OnTouchListener{

    TextView tv;
    View triangulationViev;
    SeekBar sbScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textNum);
        triangulationViev = findViewById(R.id.triangulationView);
        triangulationViev.setOnTouchListener(this);
        sbScale = findViewById(R.id.seekBar);
        sbScale.setOnSeekBarChangeListener(this);
    }

    float mx, my, curX,curY;

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                v.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
        }
        return true;
    }


    public void OnClick(View v){
        if (Triangulation.getInstance().currentStep<Triangulation.getInstance().numOfPoints-1) {
            Triangulation.getInstance().currentStep++;
            Triangulation.getInstance().AddPoint(Triangulation.getInstance().currentStep);
            tv.setText(String.valueOf(Triangulation.getInstance().currentStep));
            triangulationViev.invalidate();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Triangulation.getInstance().scale = progress+1;
        //triangulationViev.setScaleX((float)(progress*0.2)+1);
       // triangulationViev.setScaleY((float)(progress*0.2)+1);
       // triangulationViev.setScaleX((float)(0.5));
       // triangulationViev.setScaleY((float)(0.5));
        triangulationViev.invalidate();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}