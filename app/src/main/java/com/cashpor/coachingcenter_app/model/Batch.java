package com.cashpor.coachingcenter_app.model;

public class Batch {
    public int srNo;
    public String name;
    public String board;
    public String medium;
    public String sClass;
    public String createdDate;
    public boolean selected;

    public Batch(int srNo, String name, String board, String medium, String sClass, String createdDate) {
        this.srNo = srNo;
        this.name = name;
        this.board = board;
        this.medium = medium;
        this.sClass = sClass;
        this.createdDate = createdDate;
        this.selected = false;
    }
}
