package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        final Button settingButton = findViewById(R.id.settingButton);

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

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilder.setView(promptsView);

                final EditText work = promptsView.findViewById(R.id.workTime);
                final EditText rest = promptsView.findViewById(R.id.restTime);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        timerState.reset();

                                        String workTime = work.getText().toString();
                                        String restTime = rest.getText().toString();

                                        if (!workTime.isEmpty()) {
                                            Calendar time = Calendar.getInstance();
                                            time.set(Calendar.SECOND, Integer.valueOf(workTime));
                                            time.set(Calendar.MINUTE, 0);
                                            timerState.setWorkPeriod(time);
                                        }

                                        if (!restTime.isEmpty()) {
                                            Calendar time = Calendar.getInstance();
                                            time.set(Calendar.SECOND, Integer.valueOf(restTime));
                                            time.set(Calendar.MINUTE, 0);
                                            timerState.setRestPeriod(time);
                                        }

                                        timerState.reset();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

    }
}
