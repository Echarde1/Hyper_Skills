package com.example.myapplication.util;

import android.support.test.espresso.IdlingResource;

public class EspressoCountingIdling {

    private static final String RESOURCE = "Global";

    private static SimpleCountingIdling countingIdling = new SimpleCountingIdling(RESOURCE);

    public static void increment() {
        countingIdling.increment();
    }

    public static void decrement() {
        countingIdling.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return countingIdling;
    }

}
