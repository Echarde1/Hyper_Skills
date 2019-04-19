package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Timer.TimerState;
import com.example.myapplication.Timer.TimerView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TimerState timerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button startButton = findViewById(R.id.startButton);
        final Button resetButton = findViewById(R.id.resetButton);

        final TimerView timerView = findViewById(R.id.timerView);

        Calendar work = Calendar.getInstance();
        work.set(Calendar.MINUTE, 0);
        work.set(Calendar.SECOND, 5);

        Calendar rest = Calendar.getInstance();
        rest.set(Calendar.MINUTE, 0);
        rest.set(Calendar.SECOND, 3);

        timerState = new TimerState(work, rest, 1, timerView, this);
        timerState.reset();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerState.start();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerState.reset();
            }
        });

    }
}
