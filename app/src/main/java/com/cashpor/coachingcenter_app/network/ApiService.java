package com.cashpor.coachingcenter_app.network;

import com.cashpor.coachingcenter_app.network.model.LoginRequest;
import com.cashpor.coachingcenter_app.network.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}
