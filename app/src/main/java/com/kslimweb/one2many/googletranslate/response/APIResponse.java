package com.kslimweb.one2many.googletranslate.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResponse {

    @SerializedName("data")
    @Expose
    private TranslationData data;

    public TranslationData getData() {
        return data;
    }
}