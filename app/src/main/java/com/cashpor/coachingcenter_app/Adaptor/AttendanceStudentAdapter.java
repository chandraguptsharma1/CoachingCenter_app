package com.cashpor.coachingcenter_app.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.AttendanceStudent;

import java.util.List;

public class AttendanceStudentAdapter extends RecyclerView.Adapter<AttendanceStudentAdapter.VH> {

    private final List<AttendanceStudent> list;

    public AttendanceStudentAdapter(List<AttendanceStudent> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance_student, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        AttendanceStudent item = list.get(position);

        h.tvSrNo.setText(String.valueOf(item.srNo));
        h.tvName.setText(item.name);
        h.tvBatch.setText(item.batch);

        h.rgAttendance.setOnCheckedChangeListener(null);
        h.rgAttendance.clearCheck();

        if ("P".equals(item.attendanceStatus)) {
            h.rbPresent.setChecked(true);
        } else if ("A".equals(item.attendanceStatus)) {
            h.rbAbsent.setChecked(true);
        } else if ("L".equals(item.attendanceStatus)) {
            h.rbLate.setChecked(true);
        }

        h.rgAttendance.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPresent) {
                item.attendanceStatus = "P";
            } else if (checkedId == R.id.rbAbsent) {
                item.attendanceStatus = "A";
            } else if (checkedId == R.id.rbLate) {
                item.attendanceStatus = "L";
            } else {
                item.attendanceStatus = "";
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvSrNo, tvName, tvBatch;
        RadioGroup rgAttendance;
        RadioButton rbPresent, rbAbsent, rbLate;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvName = itemView.findViewById(R.id.tvName);
            tvBatch = itemView.findViewById(R.id.tvBatch);
            rgAttendance = itemView.findViewById(R.id.rgAttendance);
            rbPresent = itemView.findViewById(R.id.rbPresent);
            rbAbsent = itemView.findViewById(R.id.rbAbsent);
            rbLate = itemView.findViewById(R.id.rbLate);
        }
    }
}
