package com.cashpor.coachingcenter_app.UI.TestSeries;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.SetPaperModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManageTestPaper extends AppCompatActivity {

    private TableLayout tl;
    private MaterialCheckBox cbAll;
    private MaterialButton btnDelete, btnCreate;
    private Spinner spShow;
    private EditText etSearch;
    private TextView tvShowing, btnPrev, btnNext;

    private final List<SetPaperModel> all = new ArrayList<>();
    private final List<SetPaperModel> filtered = new ArrayList<>();

    private boolean updatingAll = false;
    private int pageSize = 10;
    private int page = 0;

    // widths dp
    private final int W_CB = 60;
    private final int W_SR = 90;
    private final int W_BOARD = 140;
    private final int W_MED = 150;
    private final int W_CLASS = 120;
    private final int W_SUB = 200;
    private final int W_CHAP = 200;
    private final int W_DT = 220;
    private final int W_ACTION = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_test_paper);

        tl = findViewById(R.id.tlSetPaper);
        cbAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);
        btnCreate = findViewById(R.id.btnCreate);
        spShow = findViewById(R.id.spShowEntries);
        etSearch = findViewById(R.id.etSearch);
        tvShowing = findViewById(R.id.tvShowing);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        // Spinner show entries
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"10", "25", "50", "100"});
        spShow.setAdapter(ad);
        spShow.setSelection(0);

        // Dummy data (web is empty, add some to test)
        // all.add(new SetPaperModel(1,"MHSB","English","8th Std","Science","Ch-1","24 Feb 2026 01:30 PM"));
        // all.add(new SetPaperModel(2,"CBSE","Hindi","9th Std","Maths","Ch-2","24 Feb 2026 02:10 PM"));

        applyFilter("");

        btnCreate.setOnClickListener(v ->
                startActivity(new Intent(this, CreateSetPaperDetails.class))
        );

        btnDelete.setOnClickListener(v -> {
            int removed = 0;
            for (Iterator<SetPaperModel> it = all.iterator(); it.hasNext();) {
                if (it.next().selected) { it.remove(); removed++; }
            }
            Toast.makeText(this, "Deleted: " + removed, Toast.LENGTH_SHORT).show();
            applyFilter(etSearch.getText().toString().trim());
        });

        cbAll.setOnCheckedChangeListener((b, checked) -> {
            if (updatingAll) return;
            for (SetPaperModel m : getCurrentPageItems()) m.selected = checked;
            renderTable();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                page = 0;
                applyFilter(s.toString().trim());
            }
        });

        spShow.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                pageSize = Integer.parseInt(spShow.getSelectedItem().toString());
                page = 0;
                renderTable();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        btnPrev.setOnClickListener(v -> {
            if (page > 0) { page--; renderTable(); }
        });

        btnNext.setOnClickListener(v -> {
            int maxPage = (int) Math.ceil(filtered.size() / (double) pageSize) - 1;
            if (page < maxPage) { page++; renderTable(); }
        });
    }

    private void applyFilter(String q) {
        filtered.clear();
        if (q.isEmpty()) {
            filtered.addAll(all);
        } else {
            String qq = q.toLowerCase();
            for (SetPaperModel m : all) {
                String hay = (m.board + " " + m.medium + " " + m.sClass + " " + m.subject + " " + m.chapter + " " + m.createDateTime).toLowerCase();
                if (hay.contains(qq)) filtered.add(m);
            }
        }
        renderTable();
    }

    private List<SetPaperModel> getCurrentPageItems() {
        int start = page * pageSize;
        int end = Math.min(start + pageSize, filtered.size());
        if (start >= end) return new ArrayList<>();
        return filtered.subList(start, end);
    }

    private void renderTable() {
        tl.removeAllViews();
        addHeader();

        List<SetPaperModel> pageItems = getCurrentPageItems();

        if (filtered.isEmpty()) addEmptyRow();
        else for (int i = 0; i < pageItems.size(); i++) addRow(i, pageItems.get(i));

        refreshTopUI();
        refreshFooter();
    }

    private void refreshTopUI() {
        int selected = 0;
        boolean allChecked = !getCurrentPageItems().isEmpty();
        for (SetPaperModel m : getCurrentPageItems()) {
            if (m.selected) selected++;
            else allChecked = false;
        }
        btnDelete.setText(selected + " Delete");

        updatingAll = true;
        cbAll.setChecked(allChecked);
        updatingAll = false;
    }

    private void refreshFooter() {
        int start = filtered.isEmpty() ? 0 : (page * pageSize) + 1;
        int end = Math.min((page + 1) * pageSize, filtered.size());
        tvShowing.setText("Showing " + (filtered.isEmpty() ? 0 : start) + " to " + end + " of " + filtered.size() + " entries");
    }

    private void addHeader() {
        TableRow tr = new TableRow(this);
        tr.addView(headerCell("", W_CB));
        tr.addView(headerCell("SR.NO", W_SR));
        tr.addView(headerCell("BOARD", W_BOARD));
        tr.addView(headerCell("MEDIUM", W_MED));
        tr.addView(headerCell("CLASS", W_CLASS));
        tr.addView(headerCell("SUBJECT", W_SUB));
        tr.addView(headerCell("CHAPTER", W_CHAP));
        tr.addView(headerCell("CREATE DATE / TIME", W_DT));
        tr.addView(headerCell("ACTION", W_ACTION));
        tl.addView(tr);
    }

    private void addEmptyRow() {
        TableRow tr = new TableRow(this);
        TextView tv = textCell("No data available in table", dp(W_CB+W_SR+W_BOARD+W_MED+W_CLASS+W_SUB+W_CHAP+W_DT+W_ACTION), false);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(dp(12), dp(18), dp(12), dp(18));
        tr.addView(tv);
        tl.addView(tr);
    }

    private void addRow(int index, SetPaperModel m) {
        boolean alt = index % 2 != 0;
        TableRow tr = new TableRow(this);

        CheckBox cb = new CheckBox(this);
        cb.setChecked(m.selected);
        cb.setMinWidth(dp(W_CB));
        cb.setBackgroundResource(alt ? R.drawable.bg_table_cell_alt : R.drawable.bg_table_cell);
        cb.setOnCheckedChangeListener((btn, checked) -> {
            m.selected = checked;
            refreshTopUI();
        });
        tr.addView(cb);

        tr.addView(textCell(String.valueOf(m.srNo), W_SR, alt));
        tr.addView(textCell(m.board, W_BOARD, alt));
        tr.addView(textCell(m.medium, W_MED, alt));
        tr.addView(textCell(m.sClass, W_CLASS, alt));
        tr.addView(textCell(m.subject, W_SUB, alt));
        tr.addView(textCell(m.chapter, W_CHAP, alt));
        tr.addView(textCell(m.createDateTime, W_DT, alt));

        TextView action = textCell("View", W_ACTION, alt);
        action.setGravity(Gravity.CENTER);
        action.setOnClickListener(v -> Toast.makeText(this, "Open: " + m.subject, Toast.LENGTH_SHORT).show());
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

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}