package com.cashpor.coachingcenter_app.UI.classManagement;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.ApiAttendanceItem;
import com.cashpor.coachingcenter_app.model.AttendanceListResponse;
import com.cashpor.coachingcenter_app.network.ApiClient;
import com.cashpor.coachingcenter_app.network.ApiService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceListActivity extends AppCompatActivity {

    private TableLayout tlAttendance;
    private TextView tvNoData;
    private ProgressDialog progressDialog;

    private final List<ApiAttendanceItem> attendanceList = new ArrayList<>();

    private final int W_SR = 50;
    private final int W_NAME = 160;
    private final int W_BATCH = 130;
    private final int W_DATE = 120;
    private final int W_ATTENDANCE = 100;

    private TextView tvSelectedDate;
    private MaterialButton btnSelectDate, btnAddAttendance;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        tlAttendance = findViewById(R.id.tlAttendance);
        tvNoData = findViewById(R.id.tvNoData);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading attendance...");
        progressDialog.setCancelable(false);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnAddAttendance = findViewById(R.id.btnAddAttendance);

        btnSelectDate.setOnClickListener(v -> openDatePicker());

        btnAddAttendance.setOnClickListener(v -> {
            if (selectedDate == null || selectedDate.trim().isEmpty()) {
                Toast.makeText(AttendanceListActivity.this, "Please select date first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(AttendanceListActivity.this, AddAttendanceActivity.class);
            intent.putExtra("attendance_date", selectedDate);
            startActivity(intent);
        });

        loadAttendanceFromApi();
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
        } else {
            tlAttendance.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void loadAttendanceFromApi() {
        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Token not found. Please login again.", Toast.LENGTH_LONG).show();
            updateNoDataState(false);
            return;
        }

        showLoading();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<AttendanceListResponse> call = apiService.getMyAttendance("Bearer " + token);

        call.enqueue(new Callback<AttendanceListResponse>() {
            @Override
            public void onResponse(Call<AttendanceListResponse> call, Response<AttendanceListResponse> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    AttendanceListResponse res = response.body();

                    if (res.success && res.data != null && !res.data.isEmpty()) {
                        attendanceList.clear();
                        attendanceList.addAll(res.data);
                        renderTable();
                    } else {
                        attendanceList.clear();
                        renderTable();
                        Toast.makeText(
                                AttendanceListActivity.this,
                                res.message != null ? res.message : "No attendance found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    attendanceList.clear();
                    renderTable();
                    Toast.makeText(
                            AttendanceListActivity.this,
                            "Server error: " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<AttendanceListResponse> call, Throwable t) {
                hideLoading();
                attendanceList.clear();
                renderTable();
                Toast.makeText(
                        AttendanceListActivity.this,
                        "API Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = String.format(
                            Locale.getDefault(),
                            "%04d-%02d-%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay
                    );
                    tvSelectedDate.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
    private void renderTable() {
        tlAttendance.removeAllViews();

        if (attendanceList.isEmpty()) {
            updateNoDataState(false);
            return;
        }

        updateNoDataState(true);
        addHeaderRow();

        for (int i = 0; i < attendanceList.size(); i++) {
            addDataRow(i, attendanceList.get(i));
        }
    }

    private void addHeaderRow() {
        TableRow tr = new TableRow(this);

        tr.addView(headerCell("SR.NO", W_SR));
        tr.addView(headerCell("NAME", W_NAME));
        tr.addView(headerCell("BATCH", W_BATCH));
        tr.addView(headerCell("DATE", W_DATE));
        tr.addView(headerCell("ATTENDANCE", W_ATTENDANCE));

        tlAttendance.addView(tr);
    }

    private void addDataRow(int index, ApiAttendanceItem item) {
        boolean alt = index % 2 != 0;
        TableRow tr = new TableRow(this);

        tr.addView(textCell(String.valueOf(index + 1), W_SR, alt));
        tr.addView(textCell(value(item.studentName), W_NAME, alt));
        tr.addView(textCell(value(item.batchId), W_BATCH, alt));
        tr.addView(textCell(formatDate(value(item.date)), W_DATE, alt));

        TextView tvAttendance = textCell(mapAttendance(value(item.attendance)), W_ATTENDANCE, alt);
        String att = value(item.attendance).toLowerCase();

        if ("p".equals(att)) {
            tvAttendance.setTextColor(0xFF16A34A);
            tvAttendance.setTypeface(tvAttendance.getTypeface(), Typeface.BOLD);
        } else if ("a".equals(att)) {
            tvAttendance.setTextColor(0xFFDC2626);
            tvAttendance.setTypeface(tvAttendance.getTypeface(), Typeface.BOLD);
        } else if ("l".equals(att)) {
            tvAttendance.setTextColor(0xFFF59E0B);
            tvAttendance.setTypeface(tvAttendance.getTypeface(), Typeface.BOLD);
        }

        tr.addView(tvAttendance);

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

    private String value(String s) {
        return s == null ? "" : s;
    }

    private String formatDate(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        if (raw.contains("T")) {
            return raw.substring(0, 10);
        }
        return raw;
    }

    private String mapAttendance(String value) {
        if ("p".equalsIgnoreCase(value)) return "Present";
        if ("a".equalsIgnoreCase(value)) return "Absent";
        if ("l".equalsIgnoreCase(value)) return "Late";
        return value;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAttendanceFromApi();
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}