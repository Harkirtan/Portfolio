package com.example.runningtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.provider.Settings;

/**
 * Broadcast Receiver which will play an alarm for 10 seconds when intent is received
 */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mediaPlayer=MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.start();

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                mediaPlayer.stop();
                mediaPlayer.release();
                abortBroadcast();
            }
        }.start();


    }
}
