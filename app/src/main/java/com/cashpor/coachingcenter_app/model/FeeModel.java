package com.cashpor.coachingcenter_app.model;

public class FeeModel {
    public int srNo;
    public String name, batch, board, medium, sClass;
    public double totalFee, discountPercent, actualFee, paidFee, dueFee;
    public boolean selected;

    public FeeModel(int srNo, String name, String batch, String board, String medium, String sClass,
                    double totalFee, double discountPercent, double paidFee) {
        this.srNo = srNo;
        this.name = name;
        this.batch = batch;
        this.board = board;
        this.medium = medium;
        this.sClass = sClass;
        this.totalFee = totalFee;
        this.discountPercent = discountPercent;

        // ✅ actual fee = total - discount
        this.actualFee = totalFee - (totalFee * discountPercent / 100.0);
        this.paidFee = paidFee;

        // ✅ due fee = actual - paid
        this.dueFee = Math.max(0, this.actualFee - this.paidFee);

        this.selected = false;
    }
}
