package com.cashpor.coachingcenter_app.model;

public class ObjectivePaperModel {
    public int srNo;
    public String board, medium, sClass, subject, chapter, paperDate, createDateTime;
    public boolean selected;

    public ObjectivePaperModel(int srNo, String board, String medium, String sClass,
                               String subject, String chapter, String paperDate, String createDateTime) {
        this.srNo = srNo;
        this.board = board;
        this.medium = medium;
        this.sClass = sClass;
        this.subject = subject;
        this.chapter = chapter;
        this.paperDate = paperDate;
        this.createDateTime = createDateTime;
    }
}