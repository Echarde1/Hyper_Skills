package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.myapplication.Matchers.ColorTimerMatcher;
import com.example.myapplication.Matchers.TextTimerMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TimerStateInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.myapplication", appContext.getPackageName());
    }

    @Test
    public void timerViewExist() {
        onView(withId(R.id.timerView)).perform(ViewActions.click());
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:05")));
    }

    @Test
    public void startTimer() throws InterruptedException {
        onView(withText("Start")).perform(ViewActions.click());
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.RED)));
        Thread.sleep(5000);
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.GREEN)));
        Thread.sleep(10000);
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.YELLOW)));
    }

    @Test
    public void resetTimer() throws InterruptedException {
        onView(withText("Start")).perform(ViewActions.click());
        Thread.sleep(4000);
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:00")));
        onView(withText("Reset")).perform(ViewActions.click());
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:05")));
    }

    @Test
    public void interruptTimer() throws InterruptedException {
        onView(withText("Start")).perform(ViewActions.click());
        Thread.sleep(2000);
        startTimer();
    }

}