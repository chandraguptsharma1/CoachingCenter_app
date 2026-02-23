package com.cashpor.coachingcenter_app.UI.classManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.LectureModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LectureList extends AppCompatActivity {

    private TableLayout tl;
    private MaterialCheckBox cbAll;
    private MaterialButton btnDelete, btnAdd;

    private final List<LectureModel> list = new ArrayList<>();
    private boolean updatingAll = false;

    // widths dp
    private final int W_CB = 60;
    private final int W_SR = 90;
    private final int W_BATCH = 160;
    private final int W_BOARD = 140;
    private final int W_MED = 150;
    private final int W_CLASS = 120;
    private final int W_SUB = 200;
    private final int W_CHAP = 180;
    private final int W_TOPIC = 200;
    private final int W_CREATED = 210;
    private final int W_ACTION = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        tl = findViewById(R.id.tlLecture);
        cbAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);
        btnAdd = findViewById(R.id.btnAddNew);

        // dummy rows
        list.add(new LectureModel(1, "Batch A", "MHSB", "English", "8th Std", "Science", "Ch-1", "Motion", "2025-11-08 05:52 AM"));
        list.add(new LectureModel(2, "Batch B", "MHSB", "Semi English", "10th Std", "Maths", "Ch-3", "Algebra", "2025-11-14 16:29 PM"));

        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, CreateLectureActivity.class)));

        btnDelete.setOnClickListener(v -> {
            int before = list.size();
            for (Iterator<LectureModel> it = list.iterator(); it.hasNext();) {
                if (it.next().selected) it.remove();
            }
            int removed = before - list.size();
            if (removed == 0) {
                Toast.makeText(this, "No lecture selected", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Deleted " + removed + " lecture", Toast.LENGTH_SHORT).show();
            renderTable();
        });

        cbAll.setOnCheckedChangeListener((b, checked) -> {
            if (updatingAll) return;
            for (LectureModel t : list) t.selected = checked;
            renderTable();
        });

        renderTable();
    }

    private void renderTable() {
        tl.removeAllViews();
        addHeader();
        for (int i = 0; i < list.size(); i++) addRow(i, list.get(i));
        refreshHeaderUI();
    }

    private void refreshHeaderUI() {
        int selected = 0;
        boolean all = !list.isEmpty();
        for (LectureModel t : list) {
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
        tr.addView(headerCell("BATCH", W_BATCH));
        tr.addView(headerCell("BOARD", W_BOARD));
        tr.addView(headerCell("MEDIUM", W_MED));
        tr.addView(headerCell("CLASS", W_CLASS));
        tr.addView(headerCell("SUBJECT", W_SUB));
        tr.addView(headerCell("CHAPTER", W_CHAP));
        tr.addView(headerCell("TOPIC", W_TOPIC));
        tr.addView(headerCell("CREATED DATE", W_CREATED));
        tr.addView(headerCell("ACTION", W_ACTION));
        tl.addView(tr);
    }

    private void addRow(int index, LectureModel t) {
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

        tr.addView(textCell(String.valueOf(t.srNo), W_SR, alt));
        tr.addView(textCell(t.batch, W_BATCH, alt));
        tr.addView(textCell(t.board, W_BOARD, alt));
        tr.addView(textCell(t.medium, W_MED, alt));
        tr.addView(textCell(t.sClass, W_CLASS, alt));
        tr.addView(textCell(t.subject, W_SUB, alt));
        tr.addView(textCell(t.chapter, W_CHAP, alt));
        tr.addView(textCell(t.topic, W_TOPIC, alt));
        tr.addView(textCell(t.createdDate, W_CREATED, alt));

        TextView action = textCell("🗑", W_ACTION, alt);
        action.setGravity(Gravity.CENTER);
        action.setOnClickListener(v -> {
            int pos = list.indexOf(t);
            if (pos >= 0) {
                list.remove(pos);
                Toast.makeText(this, "Deleted: " + t.topic, Toast.LENGTH_SHORT).show();
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

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}