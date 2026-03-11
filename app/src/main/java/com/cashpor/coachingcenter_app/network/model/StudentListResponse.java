package com.cashpor.coachingcenter_app.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentListResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<ApiStudent> data;
}
