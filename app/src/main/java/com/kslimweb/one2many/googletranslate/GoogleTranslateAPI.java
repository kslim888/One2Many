package com.kslimweb.one2many.googletranslate;

import com.kslimweb.one2many.googletranslate.response.APIResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoogleTranslateAPI {
    @POST("?")
    Call<APIResponse> translateWord(@Query("q") String inputWord,
                                    @Query("source") String sourceLanguage,
                                    @Query("target") String targetLanguage,
                                    @Query("model") String model,
                                    @Query("key") String apiKey);

    @POST("?")
    Call<APIResponse> translateWord(@Query("q") String inputWord,
                                    @Query("target") String targetLanguage,
                                    @Query("model") String model,
                                    @Query("key") String apiKey);
}
