package com.cashpor.coachingcenter_app.model;

public class Student {
    public int srNo;
    public String name, batch, board, medium, sClass, created;
    public boolean selected;

    public Student(int srNo, String name, String batch, String board, String medium, String sClass, String created) {
        this.srNo = srNo;
        this.name = name;
        this.batch = batch;
        this.board = board;
        this.medium = medium;
        this.sClass = sClass;
        this.created = created;
        this.selected = false;
    }
}
