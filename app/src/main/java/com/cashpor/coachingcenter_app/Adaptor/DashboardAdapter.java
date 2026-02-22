package com.cashpor.coachingcenter_app.Adaptor;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.components.DashboardCard;
import com.cashpor.coachingcenter_app.components.Legend;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.VH> {

    private final List<DashboardCard> list;

    public DashboardAdapter(List<DashboardCard> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        DashboardCard item = list.get(position);
        h.tvTitle.setText(item.title);

        h.legendBox.removeAllViews();
        for (Legend lg : item.legends) {
            LinearLayout row = new LinearLayout(h.itemView.getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);

            View dot = new View(h.itemView.getContext());
            LinearLayout.LayoutParams dotLp = new LinearLayout.LayoutParams(12, 12);
            dotLp.topMargin = 6;
            dotLp.rightMargin = 8;
            dot.setLayoutParams(dotLp);
            dot.setBackgroundColor(Color.parseColor(lg.color));

            TextView tv = new TextView(h.itemView.getContext());
            tv.setText(lg.text);
            tv.setTextSize(11);
            tv.setTextColor(Color.parseColor(lg.color));

            row.addView(dot);
            row.addView(tv);
            h.legendBox.addView(row);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle;
        LinearLayout legendBox;

        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            legendBox = itemView.findViewById(R.id.legendBox);
        }
    }
}