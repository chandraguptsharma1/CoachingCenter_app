package com.cashpor.coachingcenter_app.network;

import com.cashpor.coachingcenter_app.network.model.AddStudentRequest;
import com.cashpor.coachingcenter_app.network.model.CommonResponse;
import com.cashpor.coachingcenter_app.network.model.LoginRequest;
import com.cashpor.coachingcenter_app.network.model.LoginResponse;
import com.cashpor.coachingcenter_app.network.model.StudentListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("/api/students")
    Call<CommonResponse> addStudent(
            @Header("Authorization") String token,
            @Body AddStudentRequest request
    );

    @GET("/api/students")
    Call<StudentListResponse> getStudents(
            @Header("Authorization") String token
    );
}
