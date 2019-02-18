package com.kslimweb.one2many.client;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kslimweb.googletranslate.APIResponse;
import com.kslimweb.googletranslate.Data;
import com.kslimweb.googletranslate.GoogleTranslateAPI;
import com.kslimweb.googletranslate.GoogleTranslateClient;
import com.kslimweb.googletranslate.Translation;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kslimweb.googletranslate.GoogleTranslateClient.TRANSLATION_API_KEY;

class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    private TextView outputText;
    private MaterialSpinner languageSpinner;
    private Context context;

    Utils(TextView outputText, MaterialSpinner languageSpinner, Context context) {
        this.outputText = outputText;
        this.languageSpinner = languageSpinner;
        this.context = context;
    }

    void translateText(String inputText, String targetLanguage) {

        GoogleTranslateAPI googleTranslateAPI = GoogleTranslateClient.getClient().create(GoogleTranslateAPI.class);

        googleTranslateAPI.translateWord(inputText,
                targetLanguage,
                TRANSLATION_API_KEY).enqueue(new Callback<APIResponse>() {
            @Override
            @ParametersAreNonnullByDefault
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                Log.d(TAG, response.toString());

                if (response.code() == 200 && response.body() != null) {
                    Log.d(TAG, response.toString());

                    APIResponse apiResponse = response.body();
                    Data dataResponse = apiResponse.getData();
                    List<Translation> translationList = dataResponse.getTranslations();

                    String translatedText = translationList.get(0).getTranslatedText();
                    outputText.setText(translatedText);
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

    String getSpinnerSelectedLanguage() {
        int languagePosition = languageSpinner.getSelectedIndex();
        Log.d(TAG, "getSpinnerSelectedLanguage Position: " + languagePosition);

        String languageCode = "";
        switch (languagePosition) {
            case 0:
                languageCode = "ar";
                break;
            case 1:
                languageCode = "zh-CN";
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
        return languageCode;
    }
}
