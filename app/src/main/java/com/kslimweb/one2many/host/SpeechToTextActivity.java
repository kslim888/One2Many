package com.kslimweb.one2many.host;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kslimweb.firebasemessasging.FirebaseCloudMessagingService;
import com.kslimweb.one2many.R;
import java.util.ArrayList;
import java.util.Locale;

import static com.kslimweb.one2many.host.ShowQRCodeActivity.SUBSCRIBE_TOPIC;

public class SpeechToTextActivity extends AppCompatActivity {

    private static final String TAG = SpeechToTextActivity.class.getSimpleName();

    final int REQ_CODE_SPEECH_INPUT = 100;
    TextView speechToText;
    TextToSpeech textToSpeech;
    MaterialSpinner inputSpeechSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        speechToText = findViewById(R.id.speech_to_text);
        inputSpeechSpinner = findViewById(R.id.input_speech_spinner);
        setInputSpeechLanguage();

        final Button speechToTextButton = findViewById(R.id.speech_to_text_button);
        speechToTextButton.setOnClickListener(v -> inputSpeech());

        // outputSpeech method associated with this object listener callback
        textToSpeech = new TextToSpeech(this, this::checkCanSpeakLanguage);

        checkAndSubscribeTopic();

        // Check list of supported input language for that device
//        getSupportedInputLanguages();
    }

    private void setInputSpeechLanguage() {
        String [] languages = getResources().getStringArray(R.array.list_of_language);
        inputSpeechSpinner.setItems(languages);
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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getSelectedInputLanguage());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 100);
        }
    }

    private String getSelectedInputLanguage() {
        int languagePosition = inputSpeechSpinner.getSelectedIndex();
        Log.d(TAG, "getSpinnerSelectedLanguage Position: " + languagePosition);

        String languageCode = "";
        switch (languagePosition) {
            case 0:
                languageCode = "ar";
                break;
            case 1:
                languageCode = "zh";
                break;
            case 2:
                languageCode = "en";
                break;
            case 3:
                languageCode = "fr";
                break;
            case 4:
                languageCode = "gsw";
                break;
            case 5:
                languageCode = "hi";
                break;
            case 6:
                languageCode = "it";
                break;
            case 7:
                languageCode = "ja";
                break;
            case 8:
                languageCode = "ko";
                break;
            case 9:
                languageCode = "ms_MY";
                break;
            case 10:
                languageCode = "pa";
                break;
            case 11:
                languageCode = "ta";
                break;
            case 12:
                languageCode = "fil";
                break;
            case 13:
                languageCode = "th";
                break;
            case 14:
                languageCode = "vi";
                break;
        }
        textToSpeech.setLanguage(new Locale(languageCode));
        return languageCode;
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

                // TODO uncomment to speak the text
                outputSpeech();
                new FirebaseCloudMessagingService().sendNotification("One2Many", speechToText);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void getSupportedInputLanguages() {
        Locale[] languages = Locale.getAvailableLocales();
        for (Locale language : languages) {
            Log.d(TAG, "Support Input Language " + language.getDisplayName() + " " + language.toLanguageTag());
        }
    }

    private void checkCanSpeakLanguage(int status) {
        if (status == TextToSpeech.SUCCESS) {

            String language = Locale.getDefault().getDisplayLanguage();
            int result = textToSpeech.setLanguage(Locale.getDefault());

            Log.d(TAG, "Available Text to Speech Language");
            Log.d(TAG, "================================");
            Log.d(TAG, textToSpeech.getAvailableLanguages().toString());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d(TAG, "textToSpeech: Not supported output language " + language);
            } else {
                Log.d(TAG, "textToSpeech: Can output language " + language);
            }
        } else {
            Log.d(TAG, "textToSpeech: Text To Speech Initialization failed");
        }
    }

    void outputSpeech() {
        textToSpeech.speak(speechToText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }
}
