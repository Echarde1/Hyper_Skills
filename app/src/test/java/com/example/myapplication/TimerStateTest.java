package com.example.myapplication;

import com.example.myapplication.Timer.TimerState;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimerTask;

import static org.junit.Assert.assertEquals;

public class TimerStateTest {

    private static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat timeFormat = new SimpleDateFormat(
            "mm:ss", Locale.getDefault());
    private TimerState timerState;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
        }
    };

    @Before
    public void setUp() {
        calendar.set(Calendar.SECOND, 2);
        calendar.set(Calendar.MINUTE, 0);
        timerState = new TimerState(calendar);
    }

    @Test
    public void creation() {
        assertEquals(timerState.getTime(), timeFormat.format(calendar.getTime()));
    }

    @Test
    public void start() throws InterruptedException {
        timerState.start(timerTask, 1000, 1000);
        Thread.sleep(4000);
        Calendar timeOut = Calendar.getInstance();
        timeOut.set(Calendar.MINUTE, 0);
        timeOut.set(Calendar.SECOND, 0);
        assertEquals(timerState.getTime(), timeFormat.format(timeOut.getTime()));
    }

    @Test
    public void reset() throws InterruptedException {
        timerState.start(timerTask, 1000, 1000);
        Thread.sleep(4000);
        timerState.reset();
        assertEquals(timerState.getTime(), timeFormat.format(calendar.getTime()));
    }

    @Test
    public void interrupt() throws InterruptedException {
        timerState.start(timerTask, 1000, 1000);
        Thread.sleep(1000);
        timerState.start(new TimerTask() {
            @Override
            public void run() {

            }
        }, 1000, 1000);
        assertEquals(timerState.getTime(), timeFormat.format(calendar.getTime()));
    }
}