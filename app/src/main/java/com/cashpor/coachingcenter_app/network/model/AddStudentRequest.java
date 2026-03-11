package com.cashpor.coachingcenter_app.network.model;

public class AddStudentRequest {
    public String board_id;
    public String medium_id;
    public String class_id;
    public String batch_id;
    public String name;
    public String parent_name;
    public String address;
    public String phone;
    public String parent_phone;
    public String whatsapp_phone;
    public String parent_whatsapp_phone;

    public AddStudentRequest(String board_id, String medium_id, String class_id, String batch_id,
                             String name, String parent_name, String address, String phone,
                             String parent_phone, String whatsapp_phone, String parent_whatsapp_phone) {
        this.board_id = board_id;
        this.medium_id = medium_id;
        this.class_id = class_id;
        this.batch_id = batch_id;
        this.name = name;
        this.parent_name = parent_name;
        this.address = address;
        this.phone = phone;
        this.parent_phone = parent_phone;
        this.whatsapp_phone = whatsapp_phone;
        this.parent_whatsapp_phone = parent_whatsapp_phone;
    }
}
