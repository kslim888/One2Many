package com.kslimweb.one2many.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class ReceiveText extends BroadcastReceiver {

    private static final String TAG = ReceiveText.class.getSimpleName();
    TextView textView;

    public ReceiveText(TextView textView) {
        this.textView = textView;
    }

    public ReceiveText() { }

    @Override
    public void onReceive(Context context, Intent intent) {
        String messageNotification = intent.getStringExtra("MESSAGE_BODY");
        Log.d(TAG, "onReceive: " + messageNotification);
        textView.setText(messageNotification);
    }
}
