package com.example.myapplication.Matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.example.myapplication.Timer.TimerView;

import org.hamcrest.Description;

public class ColorTimerMatcher extends BoundedMatcher<View, TimerView> {

    private int color;

    public ColorTimerMatcher(int color) {
        super(TimerView.class);
        this.color = color;
    }

    public static ColorTimerMatcher withColor(int color) {
        return new ColorTimerMatcher(color);
    }

    @Override
    protected boolean matchesSafely(TimerView item) {
        return item.getColor() == color;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("getColor should return ").appendValue(color);
    }
}
