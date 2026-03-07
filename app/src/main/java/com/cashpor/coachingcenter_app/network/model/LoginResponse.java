package com.cashpor.coachingcenter_app.network.model;

public class LoginResponse {

    public int status;
    public String message;
    public String token;
    public User user;

    public static class User {
        public String id;
        public String name;
        public String email;
        public String role;
        public int roleId;
    }
}
