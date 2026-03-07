package com.cashpor.coachingcenter_app.network.model;

public class LoginRequest {

    private String email;
    private String password;
    private String fcmToken;

    public LoginRequest(String email, String password, String fcmToken) {
        this.email = email;
        this.password = password;
        this.fcmToken = fcmToken;
    }
}
