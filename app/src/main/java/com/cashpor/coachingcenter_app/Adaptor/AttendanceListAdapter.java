package com.cashpor.coachingcenter_app.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.AttendanceListItem;

import java.util.List;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.VH> {

    private final List<AttendanceListItem> list;

    public AttendanceListAdapter(List<AttendanceListItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        AttendanceListItem item = list.get(position);

        h.tvSrNo.setText(String.valueOf(item.srNo));
        h.tvStudentName.setText(item.studentName);
        h.tvBatch.setText(item.batchId);
        h.tvDate.setText(item.date);
        h.tvAttendance.setText(item.attendance);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSrNo, tvStudentName, tvBatch, tvDate, tvAttendance;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvBatch = itemView.findViewById(R.id.tvBatch);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAttendance = itemView.findViewById(R.id.tvAttendance);
        }
    }
}
