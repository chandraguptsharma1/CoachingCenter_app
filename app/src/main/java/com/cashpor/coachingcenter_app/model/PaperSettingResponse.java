package com.cashpor.coachingcenter_app.model;


import com.google.gson.annotations.SerializedName;

public class PaperSettingResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private PaperSettingData data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public PaperSettingData getData() {
        return data;
    }
}
