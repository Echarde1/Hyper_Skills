package com.example.myapplication.Timer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerState {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat(
            "mm:ss", Locale.getDefault());

    private static final int DELAY = 0;
    private static final int PERIOD = 1000;

    private State state;
    private int rounds;
    private int currentRounds;

    private Calendar workPeriod;
    private Calendar restPeriod;
    private Calendar currentPeriod;

    private TimerView timerView;
    private Timer timer;
    private Activity activity;

    public TimerState(Calendar work, Calendar rest, int rounds, TimerView timerView, Activity activity) {
        this.workPeriod = (Calendar) work.clone();
        this.restPeriod = (Calendar) rest.clone();
        this.currentPeriod = (Calendar) work.clone();
        this.timerView = timerView;
        this.rounds = rounds;
        this.currentRounds = rounds;
        this.activity = activity;
    }

    public String getTime() {
        return timeFormat.format(currentPeriod.getTime());
    }

    public State getState() {
        return state;
    }

    public void setWorkPeriod(Calendar workPeriod) {
        this.workPeriod = workPeriod;
    }

    public void setRestPeriod(Calendar restPeriod) {
        this.restPeriod = restPeriod;
    }

    public void reset() {
        state = State.Init;

        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();

        timerView.stop();
        timerView.setText(getInitTime());
        timerView.setColor(Color.RED);

        currentPeriod = (Calendar) workPeriod.clone();
        currentRounds = rounds;
    }

    public void start() {
        reset();
        state = State.Work;
        currentPeriod = (Calendar) workPeriod.clone();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerView.setText(getTime());
                    }
                });
                if (!timeOver()) {
                    currentPeriod.add(Calendar.SECOND, -1);
                } else if (state == State.Work && currentRounds > 0) {
                    sendNotification();
                    state = State.Rest;
                    currentPeriod = (Calendar) restPeriod.clone();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerView.setColor(Color.GREEN);
                            timerView.start(getSeconds());
                        }
                    });
                } else if (currentRounds == 0) {
                    state = State.End;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerView.setColor(Color.YELLOW);
                        }
                    });
                    cancel();
                } else {
                    currentRounds--;
                    state = State.Work;
                    currentPeriod = (Calendar) workPeriod.clone();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerView.setColor(Color.RED);
                            timerView.start(getSeconds());
                        }
                    });
                }
            }
        }, DELAY, PERIOD);
        timerView.start(getSeconds());
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notify = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("You need a rest!!!")
                .setContentText("It's time to stop")
                .build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notify);
    }


    private boolean timeOver() {
        return currentPeriod.get(Calendar.MINUTE) == 0 && currentPeriod.get(Calendar.SECOND) == 0;
    }


    private String getInitTime() {
        if (state == State.Work || state == State.Init) {
            return timeFormat.format(workPeriod.getTime());
        } else {
            return timeFormat.format(restPeriod.getTime());
        }
    }

    private int getSeconds() {
        if (state == State.Work || state == State.Init) {
            return workPeriod.get(Calendar.MINUTE) * 60 + workPeriod.get(Calendar.SECOND);
        } else {
            return restPeriod.get(Calendar.MINUTE) * 60 + restPeriod.get(Calendar.SECOND);
        }
    }
}
