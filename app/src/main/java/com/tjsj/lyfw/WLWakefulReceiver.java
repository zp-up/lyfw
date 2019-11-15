package com.tjsj.lyfw;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class WLWakefulReceiver extends WakefulBroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //
        String extra = intent.getStringExtra("userId");

        Intent serviceIntent = new Intent(context, MyIntentService.class);
        serviceIntent.putExtra("userId", extra);
        startWakefulService(context, serviceIntent);
    }

}
