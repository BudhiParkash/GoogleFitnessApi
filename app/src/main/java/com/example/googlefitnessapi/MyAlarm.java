package com.example.googlefitnessapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, MyService.class);
        context.startService(intent);
    }
}
