package com.cashpor.coachingcenter_app.model;

public class TestModel {
    public int srNo;
    public String testType;
    public String marks;
    public String dateTime;
    public String subject;
    public String board;
    public String medium;
    public String sClass;
    public String createdDate;
    public boolean selected;

    public TestModel(int srNo, String testType, String marks, String dateTime,
                     String subject, String board, String medium, String sClass, String createdDate) {
        this.srNo = srNo;
        this.testType = testType;
        this.marks = marks;
        this.dateTime = dateTime;
        this.subject = subject;
        this.board = board;
        this.medium = medium;
        this.sClass = sClass;
        this.createdDate = createdDate;
        this.selected = false;
    }
}
