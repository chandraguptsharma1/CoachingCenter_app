package com.cashpor.coachingcenter_app.model;

public class AttendanceListItem {
    public int id;
    public int srNo;
    public String studentName;
    public String batchId;
    public String date;
    public String attendance;

    public AttendanceListItem(int id, int srNo, String studentName, String batchId, String date, String attendance) {
        this.id = id;
        this.srNo = srNo;
        this.studentName = studentName;
        this.batchId = batchId;
        this.date = date;
        this.attendance = attendance;
    }
}
