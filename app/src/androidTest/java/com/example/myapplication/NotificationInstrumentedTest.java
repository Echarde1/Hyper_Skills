package com.example.myapplication;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NotificationInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sendNotification() throws InterruptedException {
        onView(withText("Start")).perform(ViewActions.click());
        Thread.sleep(6000);
        String expectedAppName = mActivityRule.getActivity().getString(R.string.app_name);
        String expectedContent = "It's time to stop";
        String expectedTitle = "You need a rest!!!";

        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        uiDevice.openNotification();
        uiDevice.wait(Until.hasObject(By.pkg("com.android.systemui")), 10000);
        UiObject2 title = uiDevice.findObject(By.text(expectedTitle));
        UiObject2 text = uiDevice.findObject(By.textStartsWith(expectedContent));
        assertEquals(expectedTitle, title.getText());
        assertTrue(text.getText().startsWith(expectedContent));
    }

}
