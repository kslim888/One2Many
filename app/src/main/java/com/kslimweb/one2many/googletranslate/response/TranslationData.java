package com.kslimweb.one2many.googletranslate.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslationData {

    @SerializedName("translations")
    @Expose
    private List<Translation> translations;

    public TranslationData(List<Translation> translations) {
        super();
        this.translations = translations;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}

