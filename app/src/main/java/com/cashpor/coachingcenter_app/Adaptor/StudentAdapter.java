package com.cashpor.coachingcenter_app.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.VH> {

    private final List<Student> list;

    public StudentAdapter(List<Student> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_row, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Student s = list.get(position);

        h.tvSrNo.setText(String.valueOf(s.srNo));
        h.tvName.setText(s.name);
        h.tvBatch.setText(s.batch);
        h.tvBoard.setText(s.board);
        h.tvMedium.setText(s.medium);
        h.tvClass.setText(s.sClass);
        h.tvCreated.setText(s.created);

        h.cbRow.setOnCheckedChangeListener(null);
        h.cbRow.setChecked(s.selected);
        h.cbRow.setOnCheckedChangeListener((buttonView, isChecked) -> s.selected = isChecked);

        h.btnEdit.setOnClickListener(v -> Toast.makeText(v.getContext(), "Edit: " + s.name, Toast.LENGTH_SHORT).show());
        h.btnDeleteRow.setOnClickListener(v -> Toast.makeText(v.getContext(), "Delete: " + s.name, Toast.LENGTH_SHORT).show());

        // row divider feel
        h.itemView.setBackgroundColor(position % 2 == 0 ? 0xFFFFFFFF : 0xFFF9FAFB);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        CheckBox cbRow;
        TextView tvSrNo, tvName, tvBatch, tvBoard, tvMedium, tvClass, tvCreated, btnEdit, btnDeleteRow;

        VH(@NonNull View itemView) {
            super(itemView);
            cbRow = itemView.findViewById(R.id.cbRow);
            tvSrNo = itemView.findViewById(R.id.tvSrNo);
            tvName = itemView.findViewById(R.id.tvName);
            tvBatch = itemView.findViewById(R.id.tvBatch);
            tvBoard = itemView.findViewById(R.id.tvBoard);
            tvMedium = itemView.findViewById(R.id.tvMedium);
            tvClass = itemView.findViewById(R.id.tvClass);
            tvCreated = itemView.findViewById(R.id.tvCreated);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDeleteRow = itemView.findViewById(R.id.btnDeleteRow);
        }
    }
}