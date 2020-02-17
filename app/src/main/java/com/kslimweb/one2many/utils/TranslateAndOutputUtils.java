package com.kslimweb.one2many.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kslimweb.one2many.googletranslate.response.APIResponse;
import com.kslimweb.one2many.googletranslate.response.TranslationData;
import com.kslimweb.one2many.googletranslate.GoogleTranslateAPI;
import com.kslimweb.one2many.googletranslate.GoogleTranslateClient;
import com.kslimweb.one2many.googletranslate.response.Translation;
import com.kslimweb.one2many.R;

import java.util.List;
import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kslimweb.one2many.googletranslate.GoogleTranslateClient.TRANSLATION_API_KEY;

public class TranslateAndOutputUtils {

    private static final String TAG = TranslateAndOutputUtils.class.getSimpleName();
    private final String TRANSLATE_MODEL = "nmt";

    private TextView outputText;
    private MaterialSpinner languageSpinner;
    private TextToSpeech textToSpeech;
    private Context context;

    public TranslateAndOutputUtils(TextView outputText, MaterialSpinner languageSpinner, Context context) {
        this.outputText = outputText;
        this.languageSpinner = languageSpinner;
        this.context = context;
        textToSpeech = new TextToSpeech(context, this::checkCanSpeakLanguage);
        setSpinnerItem();
    }

    public void translateText(String inputText, String targetLanguage) {

        GoogleTranslateAPI googleTranslateAPI = GoogleTranslateClient.getClient().create(GoogleTranslateAPI.class);

        // googleTranslateAPI.translateWord(inputText, "en", "ms", TRANSLATION_API_KEY) for testing
        googleTranslateAPI.translateWord(inputText,
                targetLanguage,
                TRANSLATE_MODEL,
                TRANSLATION_API_KEY).enqueue(new Callback<APIResponse>() {
            @Override
            @ParametersAreNonnullByDefault
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                Log.d(TAG, response.toString());

                if (response.code() == 200 && response.body() != null) {
                    Log.d(TAG, response.toString());

                    APIResponse apiResponse = response.body();
                    TranslationData dataResponse = apiResponse.getData();
                    List<Translation> translationList = dataResponse.getTranslations();

                    String translatedText = translationList.get(0).getTranslatedText();
                    outputText.setText(translatedText);
                    outputSpeech(translatedText);
                    Log.d(TAG, "Translated Text: " + translatedText);

                } else {
                    Toast.makeText(context, "Unable to translate", Toast.LENGTH_SHORT).show();
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

    public String getSpinnerSelectedLanguage() {
        int languagePosition = languageSpinner.getSelectedIndex();
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
                languageCode = "de";
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
                languageCode = "ms";
                break;
            case 10:
                languageCode = "pa";
                break;
            case 11:
                languageCode = "ta";
                break;
            case 12:
                languageCode = "tl";
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

    private void setSpinnerItem() {
        String [] languages = context.getResources().getStringArray(R.array.list_of_language);
        languageSpinner.setItems(languages);
    }

    private void outputSpeech(String translatedText) {
        textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);
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
}
