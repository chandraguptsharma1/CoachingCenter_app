package com.cashpor.coachingcenter_app.UI.classManagement;

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
import com.cashpor.coachingcenter_app.model.Batch;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BatchList extends AppCompatActivity {

    private TableLayout tlBatch;
    private MaterialCheckBox cbSelectAll;
    private MaterialButton btnDelete, btnAddNew;

    private final List<Batch> list = new ArrayList<>();
    private boolean updatingSelectAll = false;

    private final int W_CB = 20;
    private final int W_SR = 40;
    private final int W_NAME = 80;
    private final int W_BOARD = 80;
    private final int W_MEDIUM = 80;
    private final int W_CLASS = 30;
    private final int W_CREATED = 80;
    private final int W_ACTION = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);

        tlBatch = findViewById(R.id.tlBatch);
        cbSelectAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);
        btnAddNew = findViewById(R.id.btnAddNew);

        // Dummy data
        list.add(new Batch(1, "Batch A", "CBSE", "English", "10", "2026-02-11"));
        list.add(new Batch(2, "Batch B", "ICSE", "Hindi", "9", "2026-02-07"));
        list.add(new Batch(3, "Batch C", "CBSE", "English", "8", "2026-02-03"));

        btnAddNew.setOnClickListener(v -> openCreateBatchDialog());

        btnDelete.setOnClickListener(v -> {
            int before = list.size();
            for (Iterator<Batch> it = list.iterator(); it.hasNext();) {
                if (it.next().selected) it.remove();
            }
            int removed = before - list.size();
            if (removed == 0) {
                Toast.makeText(this, "No batch selected", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Deleted " + removed + " batch", Toast.LENGTH_SHORT).show();
            renderTable();
        });

        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (updatingSelectAll) return;
            for (Batch b : list) b.selected = isChecked;
            renderTable();
        });

        renderTable();
    }

    private void renderTable() {
        tlBatch.removeAllViews();
        addHeaderRow();

        for (int i = 0; i < list.size(); i++) {
            addDataRow(i, list.get(i));
        }

        refreshHeaderUI();
    }

    private void refreshHeaderUI() {
        int selectedCount = 0;
        boolean all = !list.isEmpty();
        for (Batch b : list) {
            if (b.selected) selectedCount++;
            else all = false;
        }

        btnDelete.setText("Delete " + selectedCount + " Selected");

        updatingSelectAll = true;
        cbSelectAll.setChecked(all);
        updatingSelectAll = false;
    }

    private void addHeaderRow() {
        TableRow tr = new TableRow(this);

        // checkbox blank header
        tr.addView(headerCell("", W_CB));
        tr.addView(headerCell("SR.NO", W_SR));
        tr.addView(headerCell("NAME", W_NAME));
        tr.addView(headerCell("BOARD", W_BOARD));
        tr.addView(headerCell("MEDIUM", W_MEDIUM));
        tr.addView(headerCell("CLASS", W_CLASS));
        tr.addView(headerCell("CREATED DATE", W_CREATED));
        tr.addView(headerCell("ACTION", W_ACTION));

        tlBatch.addView(tr);
    }

    private void addDataRow(int index, Batch b) {
        boolean alt = index % 2 != 0;
        TableRow tr = new TableRow(this);

        tr.addView(cbCell(b.selected, W_CB, alt, b));
        tr.addView(textCell(String.valueOf(b.srNo), W_SR, alt));
        tr.addView(textCell(b.name, W_NAME, alt));
        tr.addView(textCell(b.board, W_BOARD, alt));
        tr.addView(textCell(b.medium, W_MEDIUM, alt));
        tr.addView(textCell(b.sClass, W_CLASS, alt));
        tr.addView(textCell(b.createdDate, W_CREATED, alt));

        TextView action = textCell("E   D", W_ACTION, alt);
        action.setOnClickListener(v ->
                Toast.makeText(this, "Action: " + b.name, Toast.LENGTH_SHORT).show()
        );
        tr.addView(action);

        tlBatch.addView(tr);
    }

    // ---------- cell helpers ----------
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

    private CheckBox cbCell(boolean checked, int minWidthDp, boolean alt, Batch b) {
        CheckBox cb = new CheckBox(this);
        cb.setChecked(checked);
        cb.setMinWidth(dp(minWidthDp));
        cb.setPadding(dp(10), dp(10), dp(10), dp(10));
        cb.setBackgroundResource(alt ? R.drawable.bg_table_cell_alt : R.drawable.bg_table_cell);
        cb.setOnCheckedChangeListener((btn, isChecked) -> {
            b.selected = isChecked;
            refreshHeaderUI();
        });
        return cb;
    }

    private <T extends View> T wrapCell(T v) {
        TableRow.LayoutParams lp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        v.setLayoutParams(lp);

        // ✅ minimum width so columns don’t collapse
        v.setMinimumWidth(dp(90)); // normal columns
        v.setPadding(dp(12), dp(10), dp(12), dp(10));

        v.setBackgroundResource(R.drawable.bg_table_cell);
        return v;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private void openCreateBatchDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_create_batch, null);
        builder.setView(view);

        android.app.AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView btnCloseX = view.findViewById(R.id.btnClose);
        MaterialButton btnDialogClose = view.findViewById(R.id.btnDialogClose);
        MaterialButton btnAddBatch = view.findViewById(R.id.btnAddBatch);

        android.widget.Spinner spBoard = view.findViewById(R.id.spBoard);
        android.widget.Spinner spMedium = view.findViewById(R.id.spMedium);
        android.widget.Spinner spClass = view.findViewById(R.id.spClass);
        android.widget.EditText etBatchName = view.findViewById(R.id.etBatchName);

        // ✅ Dummy dropdown data (later API se aayega)
        setSpinner(spBoard, new String[]{"--- Select Board ---", "CBSE", "ICSE", "State"});
        setSpinner(spMedium, new String[]{"--- Select Medium ---", "English", "Hindi"});
        setSpinner(spClass, new String[]{"--- Select Class ---", "8", "9", "10", "11", "12"});

        View.OnClickListener close = v -> dialog.dismiss();
        btnCloseX.setOnClickListener(close);
        btnDialogClose.setOnClickListener(close);

        btnAddBatch.setOnClickListener(v -> {
            String board = spBoard.getSelectedItem().toString();
            String medium = spMedium.getSelectedItem().toString();
            String cls = spClass.getSelectedItem().toString();
            String batchName = etBatchName.getText().toString().trim();

            if (board.startsWith("---") || medium.startsWith("---") || cls.startsWith("---")) {
                Toast.makeText(this, "Please select Board/Medium/Class", Toast.LENGTH_SHORT).show();
                return;
            }
            if (batchName.isEmpty()) {
                etBatchName.setError("Enter Batch Name");
                etBatchName.requestFocus();
                return;
            }

            // ✅ Add into list (later API call)
            int sr = list.size() + 1;
            list.add(new com.cashpor.coachingcenter_app.model.Batch(sr, batchName, board, medium, cls, "2026-02-23"));

            dialog.dismiss();
            renderTable(); // refresh table
            Toast.makeText(this, "Batch added: " + batchName, Toast.LENGTH_SHORT).show();
        });
    }

    private void setSpinner(android.widget.Spinner sp, String[] items) {
        android.widget.ArrayAdapter<String> ad =
                new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sp.setAdapter(ad);
    }
}