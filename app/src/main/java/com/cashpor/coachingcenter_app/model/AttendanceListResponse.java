package com.cashpor.coachingcenter_app.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AttendanceListResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<ApiAttendanceItem> data;
}
