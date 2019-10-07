package com.kslimweb.firebasemessasging;

import com.kslimweb.firebasemessasging.response.FirebaseMessageModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FirebaseAPI {
    // TODO replace_your_own_key
    @Headers({"Authorization: key=replace_your_own_key",
            "Content-Type:application/json"})
        @POST("fcm/send")
    Call<FirebaseMessageModel> sendMessage(@Body FirebaseMessageModel message);
}
