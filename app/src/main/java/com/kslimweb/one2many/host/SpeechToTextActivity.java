package com.kslimweb.one2many.host;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kslimweb.firebasemessasging.FirebaseCloudMessagingService;
import com.kslimweb.one2many.R;
import com.kslimweb.one2many.utils.TranslateAndOutputUtils;
import java.util.ArrayList;
import java.util.Locale;

import static com.kslimweb.one2many.host.ShowQRCodeActivity.SUBSCRIBE_TOPIC;

public class SpeechToTextActivity extends AppCompatActivity {

    private static final String TAG = SpeechToTextActivity.class.getSimpleName();

    final int REQ_CODE_SPEECH_INPUT = 100;
    TextView speechToText;
    MaterialSpinner inputSpeechSpinner;
    TranslateAndOutputUtils translateAndOutputUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        speechToText = findViewById(R.id.speech_to_text);
        inputSpeechSpinner = findViewById(R.id.input_speech_spinner);

        final Button speechToTextButton = findViewById(R.id.speech_to_text_button);
        speechToTextButton.setOnClickListener(v -> inputSpeech());

        translateAndOutputUtils = new TranslateAndOutputUtils(speechToText, inputSpeechSpinner, this.getApplicationContext());

        checkAndSubscribeTopic();

        // Check list of supported input language for that device
//        getSupportedInputLanguages();
    }

    private void checkAndSubscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TOPIC)
                .addOnCompleteListener(task -> {
                    String msg = "Subscribe to " + SUBSCRIBE_TOPIC;
                    if (!task.isSuccessful()) {
                        msg = "Failed Subscribed " + SUBSCRIBE_TOPIC;
                    }
                    Log.d(TAG, msg);
                });
    }

    private void inputSpeech() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, translateAndOutputUtils.getSpinnerSelectedLanguage());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String speechToText = result.get(0);
                Log.d(TAG, "onActivityResult speechToText: " + speechToText);
                this.speechToText.setText(speechToText);
                new FirebaseCloudMessagingService().sendNotification("One2Many", speechToText);
            }
        }
    }

    private void getSupportedInputLanguages() {
        Locale[] languages = Locale.getAvailableLocales();
        for (Locale language : languages) {
            Log.d(TAG, "Support Input Language " + language.getDisplayName() + " " + language.toLanguageTag());
        }
    }
}
