package com.cashpor.coachingcenter_app.model;

public class AddAttendanceRequest {
    public int student_id;
    public String date;
    public String attendance;

    public AddAttendanceRequest(int student_id, String date, String attendance) {
        this.student_id = student_id;
        this.date = date;
        this.attendance = attendance;
    }
}
