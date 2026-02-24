package com.cashpor.coachingcenter_app.model;

public class SetPaperModel {
    public int srNo;
    public String board, medium, sClass, subject, chapter, createDateTime;
    public boolean selected;

    public SetPaperModel(int srNo, String board, String medium, String sClass,
                         String subject, String chapter, String createDateTime) {
        this.srNo = srNo;
        this.board = board;
        this.medium = medium;
        this.sClass = sClass;
        this.subject = subject;
        this.chapter = chapter;
        this.createDateTime = createDateTime;
        this.selected = false;
    }
}
