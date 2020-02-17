package com.kslimweb.one2many.firebasemessasging.response;

import com.google.gson.annotations.SerializedName;

public class FirebaseMessageModel {

    @SerializedName("to")
    String to;

    @SerializedName("notification")
    NotificationDataModel notification;

    public FirebaseMessageModel(String to, NotificationDataModel notification) {
        this.to = to;
        this.notification = notification;
    }

}
