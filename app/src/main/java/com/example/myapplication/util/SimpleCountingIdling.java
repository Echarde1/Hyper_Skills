package com.example.myapplication.util;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleCountingIdling implements IdlingResource {

    private final String name;
    private volatile ResourceCallback resourceCallback;
    private final AtomicInteger counter = new AtomicInteger(0);

    SimpleCountingIdling(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    void increment() {
        counter.getAndIncrement();
    }

    void decrement() {
        int counterVal = counter.getAndDecrement();
        if (counterVal == 0) {
            if (resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
        }
        if (counterVal < 0) {
            throw new IllegalArgumentException("Счетчик был поврежден");
        }
    }
}
