package com.kslimweb.one2many.host;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kslimweb.firebasemessasging.MyFirebaseMessagingService;
import com.kslimweb.googletranslate.APIResponse;
import com.kslimweb.googletranslate.Data;
import com.kslimweb.googletranslate.GoogleTranslateAPI;
import com.kslimweb.googletranslate.GoogleTranslateClient;
import com.kslimweb.googletranslate.Translation;
import com.kslimweb.one2many.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kslimweb.googletranslate.GoogleTranslateClient.TRANSLATION_API_KEY;
import static com.kslimweb.one2many.host.ShowQRCode.SUBSCRIBE_TOPIC;

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

        // Might unnecessary to speak result
        // outputSpeech method associated with this object
       // textToSpeech = new TextToSpeech(this, status -> checkCanSpeakLanguage(status));

        checkAndSubscribeTopic();

        // list of supported input language
        // getSupportedInputLanguages();

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
        return languageCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String speechToText = result.get(0);

                    Log.d(TAG, "onActivityResult speechToText: " + speechToText);

                    this.speechToText.setText(speechToText);

                    // outputSpeech();
                    new MyFirebaseMessagingService().sendNotification("One2Many", speechToText);

                    //translateText(speechToText);
                }
                break;
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

    public void translateText(String inputText) {
        GoogleTranslateAPI googleTranslateAPI = GoogleTranslateClient.getClient().create(GoogleTranslateAPI.class);

        googleTranslateAPI.translateWord(inputText,
                "en",
                "ms",
                TRANSLATION_API_KEY).enqueue(new Callback<APIResponse>() {
            @Override
            @ParametersAreNonnullByDefault
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                if (response.code() == 200 && response.body() != null) {
                    Log.d(TAG, response.toString());

                    APIResponse apiResponse = response.body();
                    Data dataResponse = apiResponse.getData();
                    List<Translation> translationList = dataResponse.getTranslations();

                    String translatedText = translationList.get(0).getTranslatedText();
                    // translationText.setText(translatedText);
                    Log.d(TAG, "Translated Text: " + translatedText);

                } else {
                    Toast.makeText(SpeechToTextActivity.this, "Unable to translate", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            @ParametersAreNonnullByDefault
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.d(TAG,"fail to translate");
                Log.d(TAG, "onFailure: " + t.getCause().getMessage());
            }
        });
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

            Log.d(TAG, "Avaiable Text to Speech Language");
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

    private void outputSpeech() {
        textToSpeech.speak(speechToText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

}
