package com.cashpor.coachingcenter_app.model;

import com.google.gson.annotations.SerializedName;

public class AppVersionResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private AppVersionData data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public AppVersionData getData() {
        return data;
    }
}
