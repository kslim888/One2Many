package com.kslimweb.firebasemessasging;

import com.kslimweb.firebasemessasging.response.FirebaseMessageModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FirebaseAPI {
    // TODO replace your own key
    @Headers({"Authorization: key=AAAAHbna6Jw:APA91bEzDgIkThZFIQA1om1IU4BRB9G48aP5l5IVHX6YUQfcea-B6LaxW2LySLGbQhVI2HZSJpo_dEwoJobbldlR14uCmNDGWO_g8aV5A4RLsEAgwdbCg5T_ENkCY00yd6fhkSLs7JiE",
            "Content-Type:application/json"})
        @POST("fcm/send")
    Call<FirebaseMessageModel> sendMessage(@Body FirebaseMessageModel message);
}
