package com.cashpor.coachingcenter_app.UI.classManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.UI.classManagement.CreateTest;
import com.cashpor.coachingcenter_app.model.TestModel;
import com.cashpor.coachingcenter_app.model.TestResponse;
import com.cashpor.coachingcenter_app.network.ApiClient;
import com.cashpor.coachingcenter_app.network.ApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestList extends AppCompatActivity {

    private TableLayout tl;
    private MaterialCheckBox cbAll;
    private MaterialButton btnDelete, btnAdd;

    private final List<TestModel> list = new ArrayList<>();
    private boolean updatingAll = false;

    // fixed widths (dp) to avoid broken headers
    private final int W_CB = 20;
    private final int W_SR = 40;
    private final int W_TYPE = 80;
    private final int W_MARKS = 50;
    private final int W_DT = 80;
    private final int W_SUB = 80;
    private final int W_BOARD = 80;
    private final int W_MED = 80;
    private final int W_CLASS = 80;
    private final int W_CREATED = 80;
    private final int W_ACTION = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        tl = findViewById(R.id.tlTests);
        cbAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);
        btnAdd = findViewById(R.id.btnAddNew);

        // dummy table data
        loadTests();

        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, CreateTest.class)));

        btnDelete.setOnClickListener(v -> {
            int before = list.size();
            for (Iterator<TestModel> it = list.iterator(); it.hasNext();) {
                if (it.next().selected) it.remove();
            }
            int removed = before - list.size();
            if (removed == 0) {
                Toast.makeText(this, "No test selected", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Deleted " + removed + " test", Toast.LENGTH_SHORT).show();
            renderTable();
        });

        cbAll.setOnCheckedChangeListener((b, checked) -> {
            if (updatingAll) return;
            for (TestModel t : list) t.selected = checked;
            renderTable();
        });

        renderTable();
    }

    private void renderTable() {
        tl.removeAllViews();
        addHeader();

        for (int i = 0; i < list.size(); i++) {
            addRow(i, list.get(i));
        }
        refreshHeaderUI();
    }

    private void refreshHeaderUI() {
        int selected = 0;
        boolean all = !list.isEmpty();
        for (TestModel t : list) {
            if (t.selected) selected++;
            else all = false;
        }
        btnDelete.setText("Delete " + selected + " Selected");

        updatingAll = true;
        cbAll.setChecked(all);
        updatingAll = false;
    }

    private void addHeader() {
        TableRow tr = new TableRow(this);

        tr.addView(headerCell("", W_CB));
        tr.addView(headerCell("SR.NO", W_SR));
        tr.addView(headerCell("TEST TYPE", W_TYPE));
        tr.addView(headerCell("MARKS", W_MARKS));
        tr.addView(headerCell("DATE TIME", W_DT));
        tr.addView(headerCell("SUBJECT", W_SUB));
        tr.addView(headerCell("BOARD", W_BOARD));
        tr.addView(headerCell("MEDIUM", W_MED));
        tr.addView(headerCell("CLASS", W_CLASS));
        tr.addView(headerCell("CREATED DATE", W_CREATED));
        tr.addView(headerCell("ACTION", W_ACTION));

        tl.addView(tr);
    }

    private void addRow(int index, TestModel t) {
        boolean alt = index % 2 != 0;
        TableRow tr = new TableRow(this);

        CheckBox cb = new CheckBox(this);
        cb.setChecked(t.selected);
        cb.setMinWidth(dp(W_CB));
        cb.setBackgroundResource(alt ? R.drawable.bg_table_cell_alt : R.drawable.bg_table_cell);
        cb.setOnCheckedChangeListener((btn, checked) -> {
            t.selected = checked;
            refreshHeaderUI();
        });
        tr.addView(cb);

        tr.addView(textCell(String.valueOf(index + 1), W_SR, alt));
        tr.addView(textCell(t.test_type, W_TYPE, alt));
        tr.addView(textCell(t.marks, W_MARKS, alt));
        tr.addView(textCell(t.date_time, W_DT, alt));
        tr.addView(textCell(t.subject, W_SUB, alt));
        tr.addView(textCell(t.board, W_BOARD, alt));
        tr.addView(textCell(t.medium, W_MED, alt));
        tr.addView(textCell(t.class_name, W_CLASS, alt));
        tr.addView(textCell(t.created_on, W_CREATED, alt));

        TextView action = textCell("🗑", W_ACTION, alt);
        action.setGravity(Gravity.CENTER);
        action.setOnClickListener(v -> {
            // delete single
            int pos = list.indexOf(t);
            if (pos >= 0) {
                list.remove(pos);
                Toast.makeText(this, "Deleted: " + t.subject, Toast.LENGTH_SHORT).show();
                renderTable();
            }
        });
        tr.addView(action);

        tl.addView(tr);
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

    private void loadTests() {

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getTests("Bearer " + token)
                .enqueue(new Callback<TestResponse>() {

                    @Override
                    public void onResponse(Call<TestResponse> call, Response<TestResponse> response) {

                        if (response.body() == null || response.body().data == null) {
                            Toast.makeText(TestList.this,"No data",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        list.clear();
                        list.addAll(response.body().data);

                        renderTable();
                    }

                    @Override
                    public void onFailure(Call<TestResponse> call, Throwable t) {

                        Toast.makeText(TestList.this,
                                "Failed : " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}