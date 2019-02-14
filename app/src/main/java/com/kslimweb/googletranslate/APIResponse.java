package com.kslimweb.googletranslate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public APIResponse() {
    }

    public APIResponse(Data data) {
        super();
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}