package com.cashpor.coachingcenter_app.model;

public class Student {
    public int id;
    public int srNo;
    public String name;
    public String batch;
    public String board;
    public String medium;
    public String studentClass;
    public String createdAt;
    public boolean selected;

    public Student(int id, String name, String batch, String board, String medium, String studentClass, String createdAt) {
        this.id = id;
        this.name = name;
        this.batch = batch;
        this.board = board;
        this.medium = medium;
        this.studentClass = studentClass;
        this.createdAt = createdAt;
        this.selected = false;
    }
}
