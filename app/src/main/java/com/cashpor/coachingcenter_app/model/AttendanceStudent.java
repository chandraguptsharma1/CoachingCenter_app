package com.cashpor.coachingcenter_app.model;

public class AttendanceStudent {
    public int id;
    public int srNo;
    public String name;
    public String batch;
    public String attendanceStatus = ""; // P / A / L

    public AttendanceStudent(int id, int srNo, String name, String batch) {
        this.id = id;
        this.srNo = srNo;
        this.name = name;
        this.batch = batch;
    }
}
