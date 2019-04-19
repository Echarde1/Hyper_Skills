package com.example.myapplication.Timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerState {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat(
            "mm:ss", Locale.getDefault());
    private Calendar state;
    private Calendar currentState;
    private Timer timer;

    public TimerState(Calendar state) {
        this.state = (Calendar) state.clone();
        this.currentState = (Calendar) state.clone();
    }

    public String getTime() {
        return timeFormat.format(currentState.getTime());
    }

    public void reset() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();

        currentState = (Calendar) state.clone();
    }

    public void start(TimerTask timerTask, int delay, int period) {
        reset();
        timer.schedule(timerTask, delay, period);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentState.get(Calendar.MINUTE) != 0 || currentState.get(Calendar.SECOND) != 0) {
                    currentState.add(Calendar.SECOND, -1);
                }
            }
        }, delay, period);
    }
}
