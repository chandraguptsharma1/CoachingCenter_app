package com.cashpor.coachingcenter_app.network.model;

import com.google.gson.annotations.SerializedName;

public class ApiStudent {

    @SerializedName("id")
    public int id;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("board_id")
    public String boardId;

    @SerializedName("medium_id")
    public String mediumId;

    @SerializedName("class_id")
    public String classId;

    @SerializedName("batch_id")
    public String batchId;

    @SerializedName("name")
    public String name;

    @SerializedName("parent_name")
    public String parentName;

    @SerializedName("address")
    public String address;

    @SerializedName("phone")
    public String phone;

    @SerializedName("parent_phone")
    public String parentPhone;

    @SerializedName("whatsapp_phone")
    public String whatsappPhone;

    @SerializedName("parent_whatsapp_phone")
    public String parentWhatsappPhone;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;
}
