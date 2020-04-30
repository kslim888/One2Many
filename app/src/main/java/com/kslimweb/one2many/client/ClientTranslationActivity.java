package com.kslimweb.one2many.client;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kslimweb.one2many.utils.BuildTypeUtil;
import com.kslimweb.one2many.R;
import com.kslimweb.one2many.utils.TranslateAndOutputUtils;

import static com.kslimweb.one2many.client.ScanQRCodeActivity.SUBSCRIBE_TOPIC;

public class ClientTranslationActivity extends AppCompatActivity implements MaterialSpinner.OnItemSelectedListener, MaterialSpinner.OnTouchListener {

    private static final String TAG = ClientTranslationActivity.class.getSimpleName();
    private boolean isSpinnerTouched = false;

    TextView outputText;
    MaterialSpinner targetLanguageTranslateSpinner;
    ReceiveText receiveText;
    TranslateAndOutputUtils translateAndOutputUtils;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_translation);

        outputText = findViewById(R.id.output_text);
        targetLanguageTranslateSpinner = findViewById(R.id.language_spinner);

        targetLanguageTranslateSpinner.setOnItemSelectedListener(this);
        targetLanguageTranslateSpinner.setOnTouchListener(this);

        // ReceiveText will automatically change the text view
        receiveText = new ReceiveText(outputText, targetLanguageTranslateSpinner, this.getApplicationContext());
        translateAndOutputUtils = new TranslateAndOutputUtils(outputText, targetLanguageTranslateSpinner, this.getApplicationContext());

        if (!BuildTypeUtil.isReleaseMode)
            SUBSCRIBE_TOPIC = "One2Many-test";

        checkAndSubscribeTopic();

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiveText,
                        new IntentFilter("MESSAGE"));
    }

    private void checkAndSubscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TOPIC)
                .addOnCompleteListener(task -> {
                    String msg = "Subscribe to " + SUBSCRIBE_TOPIC;
                    if (!task.isSuccessful()) {
                        msg = "Failed Subscribed to " + SUBSCRIBE_TOPIC;
                    }
                    Log.d(TAG, msg);
                });
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        if (!isSpinnerTouched) {
            return;
        }
        String targetLanguage = translateAndOutputUtils.getSpinnerSelectedLanguage();
        String originalText = outputText.getText().toString();
        Log.d(TAG, "onItemSelected Original Text: " + originalText);
        Log.d(TAG, "onItemSelected selectedLanguage: " + targetLanguage);
        translateAndOutputUtils.translateText(originalText, targetLanguage);
        isSpinnerTouched = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        isSpinnerTouched = true;
        return false;
    }
}
