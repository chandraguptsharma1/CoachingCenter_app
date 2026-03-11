package com.cashpor.coachingcenter_app.network.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("token")
    public String token;

    @SerializedName("user")
    public User user;

    public static class User {

        @SerializedName("id")
        public int id;

        @SerializedName("role_id")
        public int roleId;

        @SerializedName("emp_name")
        public String empName;

        @SerializedName("email")
        public String email;

        @SerializedName("contact_no")
        public String contactNo;

        @SerializedName("role")
        public String role;

        @SerializedName("status")
        public String status;
    }
}