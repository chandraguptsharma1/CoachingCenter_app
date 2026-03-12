package com.cashpor.coachingcenter_app.UI.classManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.Student;
import com.cashpor.coachingcenter_app.network.ApiClient;
import com.cashpor.coachingcenter_app.network.ApiService;
import com.cashpor.coachingcenter_app.network.model.ApiStudent;
import com.cashpor.coachingcenter_app.network.model.StudentListResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentListActivity extends AppCompatActivity {

    private TableLayout tlStudent;
    private MaterialCheckBox cbSelectAll;
    private MaterialButton btnDelete, btnAddNew;

    private final List<Student> students = new ArrayList<>();
    private boolean updatingSelectAll = false;

    private final int W_CB = 20;
    private final int W_SR = 40;
    private final int W_NAME = 100;
    private final int W_BATCH = 90;
    private final int W_BOARD = 90;
    private final int W_MEDIUM = 90;
    private final int W_CLASS = 60;
    private final int W_CREATED = 120;
    private final int W_ACTION = 60;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        tlStudent = findViewById(R.id.tlStudent);
        cbSelectAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);
        btnAddNew = findViewById(R.id.btnAddNew);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading students...");
        progressDialog.setCancelable(false);

        btnAddNew.setOnClickListener(v ->
                startActivity(new Intent(StudentListActivity.this, AddStudentActivity.class))
        );

        btnDelete.setOnClickListener(v -> {
            int before = students.size();

            for (Iterator<Student> it = students.iterator(); it.hasNext(); ) {
                if (it.next().selected) it.remove();
            }

            int removed = before - students.size();
            if (removed == 0) {
                Toast.makeText(this, "No students selected", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Deleted " + removed + " students", Toast.LENGTH_SHORT).show();
            renderTable();
        });

        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (updatingSelectAll) return;

            for (Student s : students) {
                s.selected = isChecked;
            }
            renderTable();
        });

        loadStudentsFromApi();
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

    private void loadStudentsFromApi() {
        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Token not found. Please login again.", Toast.LENGTH_LONG).show();
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

                    if (res.success && res.data != null) {
                        students.clear();

                        int srNo = 1;
                        for (ApiStudent apiStudent : res.data) {
                            Student localStudent = new Student(
                                    apiStudent.id,
                                    value(apiStudent.name),
                                    value(apiStudent.batchId),
                                    value(apiStudent.boardId),
                                    value(apiStudent.mediumId),
                                    value(apiStudent.classId),
                                    formatDate(value(apiStudent.createdAt))
                            );

                            localStudent.srNo = srNo++;
                            students.add(localStudent);
                        }

                        renderTable();

                    } else {
                        students.clear();
                        renderTable();
                        Toast.makeText(StudentListActivity.this,
                                res.message != null ? res.message : "No students found",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StudentListActivity.this,
                            "Server error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StudentListResponse> call, Throwable t) {
                hideLoading();
                Toast.makeText(StudentListActivity.this,
                        "API Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String value(String s) {
        return s == null ? "" : s;
    }

    private String formatDate(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        if (raw.contains("T")) {
            return raw.replace("T", " ").replace(".000Z", "");
        }
        return raw;
    }

    private void renderTable() {
        tlStudent.removeAllViews();
        addHeaderRow();

        for (int i = 0; i < students.size(); i++) {
            addDataRow(i, students.get(i));
        }

        refreshHeaderUI();
    }

    private void refreshHeaderUI() {
        int selectedCount = 0;
        boolean all = !students.isEmpty();

        for (Student s : students) {
            if (s.selected) selectedCount++;
            else all = false;
        }

        btnDelete.setText("Delete " + selectedCount + " Selected");

        updatingSelectAll = true;
        cbSelectAll.setChecked(all);
        updatingSelectAll = false;
    }

    private void addHeaderRow() {
        TableRow tr = new TableRow(this);

        tr.addView(headerCell("", W_CB));
        tr.addView(headerCell("SR.NO", W_SR));
        tr.addView(headerCell("NAME", W_NAME));
        tr.addView(headerCell("BATCH", W_BATCH));
        tr.addView(headerCell("BOARD", W_BOARD));
        tr.addView(headerCell("MEDIUM", W_MEDIUM));
        tr.addView(headerCell("CLASS", W_CLASS));
        tr.addView(headerCell("CREATED DATE", W_CREATED));
        tr.addView(headerCell("ACTION", W_ACTION));

        tlStudent.addView(tr);
    }

    private void addDataRow(int index, Student s) {
        boolean alt = index % 2 != 0;
        TableRow tr = new TableRow(this);

        tr.addView(cbCell(s.selected, W_CB, alt, s));
        tr.addView(textCell(String.valueOf(s.srNo), W_SR, alt));
        tr.addView(textCell(s.name, W_NAME, alt));
        tr.addView(textCell(s.batch, W_BATCH, alt));
        tr.addView(textCell(s.board, W_BOARD, alt));
        tr.addView(textCell(s.medium, W_MEDIUM, alt));
        tr.addView(textCell(s.studentClass, W_CLASS, alt));
        tr.addView(textCell(s.createdAt, W_CREATED, alt));

        TextView action = textCell("E   D", W_ACTION, alt);
        action.setOnClickListener(v ->
                Toast.makeText(this, "Action: " + s.name, Toast.LENGTH_SHORT).show()
        );
        tr.addView(action);

        tlStudent.addView(tr);
    }

    private TextView headerCell(String text, int minWidthDp) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(12);
        tv.setTextColor(0xFF111827);
        tv.setTypeface(tv.getTypeface(), android.graphics.Typeface.BOLD);
        tv.setPadding(dp(12), dp(12), dp(12), dp(12));
        tv.setSingleLine(true);
        tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
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
        tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
        tv.setMinWidth(dp(minWidthDp));
        tv.setBackgroundResource(alt ? R.drawable.bg_table_cell_alt : R.drawable.bg_table_cell);
        return tv;
    }

    private CheckBox cbCell(boolean checked, int minWidthDp, boolean alt, Student s) {
        CheckBox cb = new CheckBox(this);
        cb.setChecked(checked);
        cb.setMinWidth(dp(minWidthDp));
        cb.setPadding(dp(10), dp(10), dp(10), dp(10));
        cb.setBackgroundResource(alt ? R.drawable.bg_table_cell_alt : R.drawable.bg_table_cell);
        cb.setOnCheckedChangeListener((btn, isChecked) -> {
            s.selected = isChecked;
            refreshHeaderUI();
        });
        return cb;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentsFromApi();
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}