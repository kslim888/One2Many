package com.kslimweb.one2many.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class ReceiveText extends BroadcastReceiver {

    private static final String TAG = ReceiveText.class.getSimpleName();

    TextView outputText;
    MaterialSpinner languageSpinner;
    Utils utils;

    public ReceiveText(TextView outputText, MaterialSpinner languageSpinner, Context context) {
        this.outputText = outputText;
        this.languageSpinner = languageSpinner;
        utils = new Utils(outputText, languageSpinner, context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String speechToText = intent.getStringExtra("MESSAGE_BODY");
        String targetLanguage = utils.getSpinnerSelectedLanguage();
        Log.d(TAG, "onReceive: " + speechToText);
        utils.translateText(speechToText, targetLanguage);
    }
}
