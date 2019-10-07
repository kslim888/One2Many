package com.kslimweb.googletranslate;

import com.kslimweb.googletranslate.response.APIResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoogleTranslateAPI {
    @POST("?")
    Call<APIResponse> translateWord(@Query("q") String inputWord,
                                    @Query("source") String sourceLanguage,
                                    @Query("target") String targetLanguage,
                                    @Query("key") String apiKey);

    @POST("?")
    Call<APIResponse> translateWord(@Query("q") String inputWord,
                                           @Query("target") String targetLanguage,
                                           @Query("key") String apiKey);
}
