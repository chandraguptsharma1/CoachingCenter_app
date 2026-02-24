package com.cashpor.coachingcenter_app.UI.classManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class SetPaperList extends AppCompatActivity {

    private TableLayout tl;
    private MaterialCheckBox cbAll;
    private MaterialButton btnDelete, btnCreate;
    private EditText etSearch;

    private final List<SetPaperModel> list = new ArrayList<>();
    private boolean updatingAll = false;

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
        setContentView(R.layout.activity_set_paper_list);

        tl = findViewById(R.id.tlSetPaper);
        cbAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);
        btnCreate = findViewById(R.id.btnCreate);
        etSearch = findViewById(R.id.etSearch);

        // dummy (screenshot currently empty)
        // add dummy to test UI:
        // list.add(new SetPaperModel(1,"MHSB","English","8th Std","Science","Ch-1","24 Feb 2026 01:30 PM"));

        btnCreate.setOnClickListener(v ->
                startActivity(new Intent(this, CreateSetPaper.class))
        );

        btnDelete.setOnClickListener(v -> {
            int before = list.size();
            for (Iterator<SetPaperModel> it = list.iterator(); it.hasNext();) {
                if (it.next().selected) it.remove();
            }
            int removed = before - list.size();
            Toast.makeText(this, "Deleted: " + removed, Toast.LENGTH_SHORT).show();
            renderTable();
        });

        cbAll.setOnCheckedChangeListener((b, checked) -> {
            if (updatingAll) return;
            for (SetPaperModel t : list) t.selected = checked;
            renderTable();
        });

        // Search UI (optional simple filter later)
        // abhi UI only, no filter

        renderTable();
    }

    private void renderTable() {
        tl.removeAllViews();
        addHeader();

        if (list.isEmpty()) {
            addEmptyRow();
        } else {
            for (int i = 0; i < list.size(); i++) addRow(i, list.get(i));
        }
        refreshTopUI();
    }

    private void refreshTopUI() {
        int selected = 0;
        boolean all = !list.isEmpty();
        for (SetPaperModel t : list) {
            if (t.selected) selected++;
            else all = false;
        }
        btnDelete.setText(selected + " Delete");

        updatingAll = true;
        cbAll.setChecked(all);
        updatingAll = false;
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

        TextView tv = textCell("No data available in table", dp(W_CB + W_SR + W_BOARD + W_MED + W_CLASS + W_SUB + W_CHAP + W_DT + W_ACTION), false);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(dp(12), dp(18), dp(12), dp(18));
        tr.addView(tv);

        tl.addView(tr);
    }

    private void addRow(int index, SetPaperModel t) {
        boolean alt = index % 2 != 0;
        TableRow tr = new TableRow(this);

        CheckBox cb = new CheckBox(this);
        cb.setChecked(t.selected);
        cb.setMinWidth(dp(W_CB));
        cb.setBackgroundResource(alt ? R.drawable.bg_table_cell_alt : R.drawable.bg_table_cell);
        cb.setOnCheckedChangeListener((btn, checked) -> {
            t.selected = checked;
            refreshTopUI();
        });
        tr.addView(cb);

        tr.addView(textCell(String.valueOf(t.srNo), W_SR, alt));
        tr.addView(textCell(t.board, W_BOARD, alt));
        tr.addView(textCell(t.medium, W_MED, alt));
        tr.addView(textCell(t.sClass, W_CLASS, alt));
        tr.addView(textCell(t.subject, W_SUB, alt));
        tr.addView(textCell(t.chapter, W_CHAP, alt));
        tr.addView(textCell(t.createDateTime, W_DT, alt));

        TextView action = textCell("View", W_ACTION, alt);
        action.setGravity(Gravity.CENTER);
        action.setOnClickListener(v ->
                Toast.makeText(this, "Open Paper: " + t.subject, Toast.LENGTH_SHORT).show()
        );
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