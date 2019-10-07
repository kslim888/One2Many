package com.kslimweb.firebasemessasging;

import com.google.gson.annotations.SerializedName;

class FirebaseMessageModel {

    @SerializedName("to")
    String to;

    @SerializedName("notification")
    NotificationDataModel notification;

    FirebaseMessageModel(String to, NotificationDataModel notification) {
        this.to = to;
        this.notification = notification;
    }

}
