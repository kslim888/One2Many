package com.kslimweb.googletranslate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("translations")
    @Expose
    private List<Translation> translations = null;

    public Data() {
    }

    public Data(List<Translation> translations) {
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

