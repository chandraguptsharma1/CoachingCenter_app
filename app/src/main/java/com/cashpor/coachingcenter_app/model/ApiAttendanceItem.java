package com.cashpor.coachingcenter_app.model;

import com.google.gson.annotations.SerializedName;

public class ApiAttendanceItem {

    @SerializedName("id")
    public int id;

    @SerializedName("student_id")
    public int studentId;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("student_name")
    public String studentName;

    @SerializedName("board_id")
    public String boardId;

    @SerializedName("medium_id")
    public String mediumId;

    @SerializedName("class_id")
    public String classId;

    @SerializedName("batch_id")
    public String batchId;

    @SerializedName("date")
    public String date;

    @SerializedName("attendance")
    public String attendance;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;
}
