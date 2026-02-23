package com.cashpor.coachingcenter_app.model;

public class LectureModel {
    public int srNo;
    public String batch;
    public String board;
    public String medium;
    public String sClass;
    public String subject;
    public String chapter;
    public String topic;
    public String createdDate;
    public boolean selected;

    public LectureModel(int srNo, String batch, String board, String medium, String sClass,
                        String subject, String chapter, String topic, String createdDate) {
        this.srNo = srNo;
        this.batch = batch;
        this.board = board;
        this.medium = medium;
        this.sClass = sClass;
        this.subject = subject;
        this.chapter = chapter;
        this.topic = topic;
        this.createdDate = createdDate;
        this.selected = false;
    }
}
