package com.cashpor.coachingcenter_app.network;

import com.cashpor.coachingcenter_app.model.AddAttendanceRequest;
import com.cashpor.coachingcenter_app.model.AddAttendanceResponse;
import com.cashpor.coachingcenter_app.model.AppVersionResponse;
import com.cashpor.coachingcenter_app.model.AttendanceListResponse;
import com.cashpor.coachingcenter_app.model.PaperSettingResponse;
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
import retrofit2.http.Query;

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

    @GET("api/student-attendance/my")
    Call<AttendanceListResponse> getMyAttendance(@Header("Authorization") String token);

    @POST("/api/student-attendance")
    Call<AddAttendanceResponse> addAttendance(
            @Header("Authorization") String token,
            @Body AddAttendanceRequest request
    );

    @GET("api/paper-settings/my")
    Call<PaperSettingResponse> getMyPaperSetting(
            @Header("Authorization") String token
    );

    @GET("api/app-version")
    Call<AppVersionResponse> getAppVersion(
            @Query("app_name") String appName
    );
}
