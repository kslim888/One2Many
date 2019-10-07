package com.kslimweb.one2many.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kslimweb.one2many.utils.TranslateAndOutputUtils;

public class ReceiveText extends BroadcastReceiver {

    private static final String TAG = ReceiveText.class.getSimpleName();

    TextView outputText;
    MaterialSpinner languageSpinner;
    TranslateAndOutputUtils translateAndOutputUtils;

    public ReceiveText(TextView outputText, MaterialSpinner languageSpinner, Context context) {
        this.outputText = outputText;
        this.languageSpinner = languageSpinner;
        translateAndOutputUtils = new TranslateAndOutputUtils(outputText, languageSpinner, context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String speechToText = intent.getStringExtra("MESSAGE_BODY");
        String targetLanguage = translateAndOutputUtils.getSpinnerSelectedLanguage();
        Log.d(TAG, "onReceive: " + speechToText);
        translateAndOutputUtils.translateText(speechToText, targetLanguage);
    }
}
