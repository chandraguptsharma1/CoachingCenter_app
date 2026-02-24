package com.cashpor.coachingcenter_app.UI.classManagement;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.FeeModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FeeList extends AppCompatActivity {

    private TableLayout tl;
    private final List<FeeModel> list = new ArrayList<>();
    private final DecimalFormat df = new DecimalFormat("0.##");

    // widths dp (so header never breaks)
    private final int W_SR = 90;
    private final int W_NAME = 180;
    private final int W_BATCH = 140;
    private final int W_BOARD = 140;
    private final int W_MED = 150;
    private final int W_CLASS = 120;
    private final int W_TOTAL = 140;
    private final int W_DISCOUNT = 200;
    private final int W_ACTUAL = 140;
    private final int W_PAID = 140;
    private final int W_DUE = 140;
    private final int W_ACTION = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_list);

        tl = findViewById(R.id.tlFees);

        // dummy data (later API)
        list.add(new FeeModel(1, "Rajesh Ghawade", "Batch A", "MHSB", "English", "8th Std", 5000, 10, 2000));
        list.add(new FeeModel(2, "Subhash Ghule", "Batch B", "CBSE", "Hindi", "9th Std", 6000, 0, 3000));
        list.add(new FeeModel(3, "Aman More", "Batch A", "ICSE", "English", "10th Std", 7000, 5, 7000));

        renderTable();
    }

    private void renderTable() {
        tl.removeAllViews();
        addHeader();
        for (int i = 0; i < list.size(); i++) addRow(i, list.get(i));
    }

    private void addHeader() {
        TableRow tr = new TableRow(this);

        tr.addView(headerCell("SR.NO", W_SR));
        tr.addView(headerCell("NAME", W_NAME));
        tr.addView(headerCell("BATCH", W_BATCH));
        tr.addView(headerCell("BOARD", W_BOARD));
        tr.addView(headerCell("MEDIUM", W_MED));
        tr.addView(headerCell("CLASS", W_CLASS));
        tr.addView(headerCell("TOTAL FEE", W_TOTAL));
        tr.addView(headerCell("DISCOUNT ON FEE (IN %)", W_DISCOUNT));
        tr.addView(headerCell("ACTUAL FEE", W_ACTUAL));
        tr.addView(headerCell("PAID FEE", W_PAID));
        tr.addView(headerCell("DUE FEE", W_DUE));
        tr.addView(headerCell("ACTION", W_ACTION));

        tl.addView(tr);
    }

    private void addRow(int index, FeeModel f) {
        boolean alt = index % 2 != 0;
        TableRow tr = new TableRow(this);

        tr.addView(textCell(String.valueOf(f.srNo), W_SR, alt));
        tr.addView(textCell(f.name, W_NAME, alt));
        tr.addView(textCell(f.batch, W_BATCH, alt));
        tr.addView(textCell(f.board, W_BOARD, alt));
        tr.addView(textCell(f.medium, W_MED, alt));
        tr.addView(textCell(f.sClass, W_CLASS, alt));
        tr.addView(textCell(df.format(f.totalFee), W_TOTAL, alt));
        tr.addView(textCell(df.format(f.discountPercent) + "%", W_DISCOUNT, alt));
        tr.addView(textCell(df.format(f.actualFee), W_ACTUAL, alt));
        tr.addView(textCell(df.format(f.paidFee), W_PAID, alt));
        tr.addView(textCell(df.format(f.dueFee), W_DUE, alt));

        TextView action = textCell("View / Pay", W_ACTION, alt);
        action.setGravity(Gravity.CENTER);
        action.setOnClickListener(v ->
                Toast.makeText(this, "Open Fee: " + f.name, Toast.LENGTH_SHORT).show()
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