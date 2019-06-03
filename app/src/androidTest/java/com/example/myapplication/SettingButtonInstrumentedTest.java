package com.example.myapplication;

import android.graphics.Color;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.myapplication.Matchers.ColorTimerMatcher;
import com.example.myapplication.Matchers.TextTimerMatcher;
import com.example.myapplication.util.EspressoCountingIdling;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SettingButtonInstrumentedTest {

    // Check `AlertController().setupButtons(...)`. These ids are being set there
    private static final int DIALOG_POSITIVE_BUTTON_ID = 16908313;
    private static final int DIALOG_NEGATIVE_BUTTON_ID = 16908314;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        EspressoCountingIdling.setCounterToZero();
    }

    @Test
    public void settingButtonExist() {
        onView(withText("Settings")).check(matches(isDisplayed()));
    }

    @Test
    public void openDialog() {
        onView(withText("Settings")).perform(click());
        onView(withId(DIALOG_POSITIVE_BUTTON_ID)).check(matches(isDisplayed()));
        onView(withId(DIALOG_NEGATIVE_BUTTON_ID)).check(matches(isDisplayed()));
    }

    @Test
    public void setWorkTime() throws InterruptedException {
        onView(withText("Settings")).perform(click());
        onView(withId(R.id.workTime)).perform(typeText("6"));
        onView(withId(DIALOG_POSITIVE_BUTTON_ID)).perform(click());

        IdlingRegistry.getInstance().register(EspressoCountingIdling.getIdlingResource());

        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:06")));
        onView(withText("Start")).perform(click());
        Thread.sleep(6000);
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.GREEN)));
    }

    @Test
    public void setRestTime() throws InterruptedException {
        onView(withText("Settings")).perform(click());
        onView(withId(R.id.restTime)).perform(typeText("5"));
        onView(withId(DIALOG_POSITIVE_BUTTON_ID)).perform(click());

        IdlingRegistry.getInstance().register(EspressoCountingIdling.getIdlingResource());

        onView(withText("Start")).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.timerView)).check(matches(TextTimerMatcher.withText("00:00")));
        onView(withId(R.id.timerView)).check(matches(ColorTimerMatcher.withColor(Color.GREEN)));
    }

    @After
    public void release() {
        for (IdlingResource resource : IdlingRegistry.getInstance().getResources()) {
            IdlingRegistry.getInstance().unregister(resource);
        }
    }
}
