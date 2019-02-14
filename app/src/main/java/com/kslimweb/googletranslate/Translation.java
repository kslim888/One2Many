package com.kslimweb.googletranslate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translation {

    @SerializedName("translatedText")
    @Expose
    private String translatedText;

    @SerializedName("detectedSourceLanguage")
    @Expose
    private String detectedSourceLanguage;

    public Translation() {
    }

    public Translation(String translatedText, String detectedSourceLanguage) {
        super();
        this.translatedText = translatedText;
        this.detectedSourceLanguage = detectedSourceLanguage;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getDetectedSourceLanguage() {
        return detectedSourceLanguage;
    }

    public void setDetectedSourceLanguage(String detectedSourceLanguage) {
        this.detectedSourceLanguage = detectedSourceLanguage;
    }
}