package com.example.myapplication;

import com.example.myapplication.Timer.State;
import com.example.myapplication.Timer.TimerState;
import com.example.myapplication.Timer.TimerView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class TimerStateTest {

    private static final Calendar work = Calendar.getInstance();
    private static final Calendar rest = Calendar.getInstance();
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    MainActivity mainActivity;
    @Mock
    TimerView timerView;

    private static SimpleDateFormat timeFormat = new SimpleDateFormat(
            "mm:ss", Locale.getDefault());
    private TimerState timerState;

    @Before
    public void setUp() {
        work.set(Calendar.SECOND, 1);
        work.set(Calendar.MINUTE, 0);
        rest.set(Calendar.SECOND, 1);
        rest.set(Calendar.MINUTE, 0);
        timerState = new TimerState(work, rest, 1, timerView, mainActivity);
    }

    @Test
    public void creation() {
        assertEquals(timerState.getTime(), timeFormat.format(work.getTime()));
    }

    @Test
    public void start() throws InterruptedException {
        timerState.start();
        assertEquals(State.Work, timerState.getState());
        Thread.sleep((work.get(Calendar.SECOND) + 1) * 1000);
        assertEquals(State.Rest, timerState.getState());
        Thread.sleep((rest.get(Calendar.SECOND) + 1) * 1000);
        assertEquals(State.Work, timerState.getState());
        Thread.sleep((work.get(Calendar.SECOND) + 1) * 1000);
        assertEquals(State.End, timerState.getState());
    }

    @Test
    public void reset() throws InterruptedException {
        timerState.start();
        Thread.sleep((work.get(Calendar.SECOND) + 1) * 1000);
        timerState.reset();
        assertEquals(State.Init, timerState.getState());
        assertEquals(timeFormat.format(work.getTime()), timerState.getTime());
    }

    @Test
    public void interrupt() throws InterruptedException {
        timerState.start();
        assertEquals(State.Work, timerState.getState());
        Thread.sleep((work.get(Calendar.SECOND) + 1) * 1000);
        start();
    }
}