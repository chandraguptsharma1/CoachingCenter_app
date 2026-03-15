package com.cashpor.coachingcenter_app.network;

import com.cashpor.coachingcenter_app.model.AddAttendanceRequest;
import com.cashpor.coachingcenter_app.model.AddAttendanceResponse;
import com.cashpor.coachingcenter_app.model.AddBatchRequest;
import com.cashpor.coachingcenter_app.model.AppVersionResponse;
import com.cashpor.coachingcenter_app.model.AttendanceListResponse;
import com.cashpor.coachingcenter_app.model.BatchResponse;
import com.cashpor.coachingcenter_app.model.BoardResponse;
import com.cashpor.coachingcenter_app.model.ClassResponse;
import com.cashpor.coachingcenter_app.model.GenericResponse;
import com.cashpor.coachingcenter_app.model.MediumResponse;
import com.cashpor.coachingcenter_app.model.PaperSettingResponse;
import com.cashpor.coachingcenter_app.model.TestResponse;
import com.cashpor.coachingcenter_app.network.model.AddStudentRequest;
import com.cashpor.coachingcenter_app.network.model.CommonResponse;
import com.cashpor.coachingcenter_app.network.model.LoginRequest;
import com.cashpor.coachingcenter_app.network.model.LoginResponse;
import com.cashpor.coachingcenter_app.network.model.StudentListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET("api/batch/list")
    Call<BatchResponse> getBatches(
            @Header("Authorization") String token
    );

    @GET("api/common/boards")
    Call<BoardResponse> getBoards(
            @Header("Authorization") String token
    );

    @GET("api/common/mediums")
    Call<MediumResponse> getMediums(
            @Header("Authorization") String token,
            @Query("board_id") String boardId
    );

    @GET("api/common/classes")
    Call<ClassResponse> getClasses(
            @Header("Authorization") String token,
            @Query("board_id") String boardId,
            @Query("medium_id") String mediumId
    );

    @POST("api/batch/add")
    Call<GenericResponse> addBatch(
            @Header("Authorization") String token,
            @Body AddBatchRequest request
    );

    @DELETE("api/batch/delete/{batch_id}")
    Call<GenericResponse> deleteBatch(
            @Header("Authorization") String token,
            @Path("batch_id") String batchId
    );

    @GET("api/test/list")
    Call<TestResponse> getTests(
            @Header("Authorization") String token
    );
}
