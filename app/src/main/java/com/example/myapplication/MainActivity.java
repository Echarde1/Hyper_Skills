package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Timer.TimerState;

import java.util.Calendar;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TimerState timerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button startButton = findViewById(R.id.startButton);
        final Button resetButton = findViewById(R.id.resetButton);
        final TextView timerView = findViewById(R.id.timerView);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 3);
        timerState = new TimerState(calendar);

        timerView.setText(timerState.getTime());

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timerView.setText(timerState.getTime());
                            }
                        });
                    }
                };

                timerState.start(timerTask, 1000, 1000);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerState.reset();
                timerView.setText(timerState.getTime());
            }
        });

    }
}
