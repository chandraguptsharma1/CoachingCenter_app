package com.cashpor.coachingcenter_app.model;

import com.google.gson.annotations.SerializedName;

public class AddAttendanceResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public Object data;
}
