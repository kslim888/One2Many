package com.kslimweb.firebasemessasging;

import com.google.gson.annotations.SerializedName;
import com.kslimweb.firebasemessasging.NotifyData;

public class FirebaseMessage {

    @SerializedName("to")
    String to;

    @SerializedName("notification")
    NotifyData notification;

    public FirebaseMessage(String to, NotifyData notification) {
        this.to = to;
        this.notification = notification;
    }

}
