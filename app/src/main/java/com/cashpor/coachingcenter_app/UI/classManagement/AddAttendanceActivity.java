package com.cashpor.coachingcenter_app.UI.classManagement;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.AddAttendanceRequest;
import com.cashpor.coachingcenter_app.model.AddAttendanceResponse;
import com.cashpor.coachingcenter_app.model.AttendanceStudent;
import com.cashpor.coachingcenter_app.network.ApiClient;
import com.cashpor.coachingcenter_app.network.ApiService;
import com.cashpor.coachingcenter_app.network.model.ApiStudent;
import com.cashpor.coachingcenter_app.network.model.StudentListResponse;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAttendanceActivity extends AppCompatActivity {

    private TextView tvDate, tvNoData;
    private TableLayout tlAttendance;
    private MaterialButton btnSubmit;
    private ProgressDialog progressDialog;

    private final List<AttendanceStudent> studentList = new ArrayList<>();
    private String selectedAttendanceDate = "";

    private final int W_SR = 50;
    private final int W_NAME = 160;
    private final int W_BATCH = 120;
    private final int W_ATTENDANCE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);

        tvDate = findViewById(R.id.tvDate);
        tvNoData = findViewById(R.id.tvNoData);
        tlAttendance = findViewById(R.id.tlAttendance);
        btnSubmit = findViewById(R.id.btnSubmit);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading students...");
        progressDialog.setCancelable(false);

        String attendanceDate = getIntent().getStringExtra("attendance_date");
        if (attendanceDate != null && !attendanceDate.trim().isEmpty()) {
            selectedAttendanceDate = attendanceDate;
        } else {
            selectedAttendanceDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }

        tvDate.setText("Date: " + selectedAttendanceDate);

        btnSubmit.setOnClickListener(v -> submitAttendance());

        loadStudents();
    }

    private void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing() && !isFinishing()) {
            progressDialog.show();
        }
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void updateNoDataState(boolean hasData) {
        if (hasData) {
            tlAttendance.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            tlAttendance.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        }
    }

    private void loadStudents() {
        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Token not found. Please login again.", Toast.LENGTH_LONG).show();
            updateNoDataState(false);
            return;
        }

        showLoading();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<StudentListResponse> call = apiService.getStudents("Bearer " + token);

        call.enqueue(new Callback<StudentListResponse>() {
            @Override
            public void onResponse(Call<StudentListResponse> call, Response<StudentListResponse> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    StudentListResponse res = response.body();

                    if (res.success && res.data != null && !res.data.isEmpty()) {
                        studentList.clear();

                        int srNo = 1;
                        for (ApiStudent apiStudent : res.data) {
                            AttendanceStudent s = new AttendanceStudent(
                                    apiStudent.id,
                                    srNo++,
                                    value(apiStudent.name),
                                    value(apiStudent.batchId)
                            );
                            studentList.add(s);
                        }

                        renderTable();
                    } else {
                        studentList.clear();
                        renderTable();
                        Toast.makeText(
                                AddAttendanceActivity.this,
                                res.message != null ? res.message : "No students found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    studentList.clear();
                    renderTable();
                    Toast.makeText(
                            AddAttendanceActivity.this,
                            "Server error: " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<StudentListResponse> call, Throwable t) {
                hideLoading();
                studentList.clear();
                renderTable();
                Toast.makeText(
                        AddAttendanceActivity.this,
                        "API Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void renderTable() {
        tlAttendance.removeAllViews();

        if (studentList.isEmpty()) {
            updateNoDataState(false);
            return;
        }

        updateNoDataState(true);
        addHeaderRow();

        for (int i = 0; i < studentList.size(); i++) {
            addDataRow(i, studentList.get(i));
        }
    }

    private void addHeaderRow() {
        TableRow tr = new TableRow(this);

        tr.addView(headerCell("SR.NO", W_SR));
        tr.addView(headerCell("NAME", W_NAME));
        tr.addView(headerCell("BATCH", W_BATCH));
        tr.addView(headerCell("ATTENDANCE", W_ATTENDANCE));

        tlAttendance.addView(tr);
    }

    private void addDataRow(int index, AttendanceStudent s) {
        boolean alt = index % 2 != 0;
        TableRow tr = new TableRow(this);

        tr.addView(textCell(String.valueOf(s.srNo), W_SR, alt));
        tr.addView(textCell(s.name, W_NAME, alt));
        tr.addView(textCell(s.batch, W_BATCH, alt));
        tr.addView(attendanceCell(s, W_ATTENDANCE, alt));

        tlAttendance.addView(tr);
    }

    private TextView headerCell(String text, int minWidthDp) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(12);
        tv.setTextColor(0xFF111827);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setPadding(dp(12), dp(12), dp(12), dp(12));
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setMinWidth(dp(minWidthDp));
        tv.setBackgroundResource(R.drawable.bg_table_header);
        return tv;
    }

    private TextView textCell(String text, int minWidthDp, boolean alt) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(12);
        tv.setTextColor(0xFF111827);
        tv.setPadding(dp(12), dp(10), dp(12), dp(10));
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setMinWidth(dp(minWidthDp));
        tv.setBackgroundResource(alt ? R.drawable.bg_table_cell_alt : R.drawable.bg_table_cell);
        return tv;
    }

    private RadioGroup attendanceCell(AttendanceStudent s, int minWidthDp, boolean alt) {
        RadioGroup rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.HORIZONTAL);

        rg.setPadding(dp(8), dp(4), dp(8), dp(4));
        rg.setBackgroundResource(alt ? R.drawable.bg_table_cell_alt : R.drawable.bg_table_cell);

        RadioButton rbPresent = new RadioButton(this);
        rbPresent.setId(View.generateViewId());
        rbPresent.setText("Present");
        rbPresent.setTextSize(12);

        RadioButton rbAbsent = new RadioButton(this);
        rbAbsent.setId(View.generateViewId());
        rbAbsent.setText("Absent");
        rbAbsent.setTextSize(12);

        RadioButton rbLate = new RadioButton(this);
        rbLate.setId(View.generateViewId());
        rbLate.setText("Late");
        rbLate.setTextSize(12);

        rg.addView(rbPresent);
        rg.addView(rbAbsent);
        rg.addView(rbLate);

        if ("p".equalsIgnoreCase(s.attendanceStatus)) {
            rbPresent.setChecked(true);
        } else if ("a".equalsIgnoreCase(s.attendanceStatus)) {
            rbAbsent.setChecked(true);
        } else if ("l".equalsIgnoreCase(s.attendanceStatus)) {
            rbLate.setChecked(true);
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == rbPresent.getId()) {
                s.attendanceStatus = "p";
            } else if (checkedId == rbAbsent.getId()) {
                s.attendanceStatus = "a";
            } else if (checkedId == rbLate.getId()) {
                s.attendanceStatus = "l";
            }
        });

        return rg;
    }

    private void submitAttendance() {
        List<AttendanceStudent> pending = new ArrayList<>();

        for (AttendanceStudent s : studentList) {
            if (s.attendanceStatus == null || s.attendanceStatus.trim().isEmpty()) {
                pending.add(s);
            }
        }

        if (!pending.isEmpty()) {
            Toast.makeText(this, "Please mark attendance for all students", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Token not found. Please login again.", Toast.LENGTH_LONG).show();
            return;
        }

        showLoading();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        final int totalCount = studentList.size();
        final int[] completedCount = {0};
        final int[] successCount = {0};
        final int[] failCount = {0};

        for (AttendanceStudent s : studentList) {
            AddAttendanceRequest request = new AddAttendanceRequest(
                    s.id,
                    selectedAttendanceDate,
                    s.attendanceStatus
            );

            Call<AddAttendanceResponse> call = apiService.addAttendance("Bearer " + token, request);

            call.enqueue(new Callback<AddAttendanceResponse>() {
                @Override
                public void onResponse(Call<AddAttendanceResponse> call, Response<AddAttendanceResponse> response) {
                    completedCount[0]++;

                    if (response.isSuccessful() && response.body() != null && response.body().success) {
                        successCount[0]++;
                    } else {
                        failCount[0]++;
                    }

                    checkAllAttendanceSubmitted(completedCount[0], totalCount, successCount[0], failCount[0]);
                }

                @Override
                public void onFailure(Call<AddAttendanceResponse> call, Throwable t) {
                    completedCount[0]++;
                    failCount[0]++;
                    checkAllAttendanceSubmitted(completedCount[0], totalCount, successCount[0], failCount[0]);
                }
            });
        }
    }

    private void checkAllAttendanceSubmitted(int completedCount, int totalCount, int successCount, int failCount) {
        if (completedCount == totalCount) {
            hideLoading();

            if (failCount == 0) {
                Toast.makeText(
                        AddAttendanceActivity.this,
                        "Attendance added successfully for all students",
                        Toast.LENGTH_LONG
                ).show();

                finish();
            } else if (successCount > 0) {
                Toast.makeText(
                        AddAttendanceActivity.this,
                        "Attendance added for " + successCount + " students, failed for " + failCount,
                        Toast.LENGTH_LONG
                ).show();

                finish();
            } else {
                Toast.makeText(
                        AddAttendanceActivity.this,
                        "Failed to add attendance",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    private String value(String s) {
        return s == null ? "" : s;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}