package com.cashpor.coachingcenter_app.UI.classManagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashpor.coachingcenter_app.Adaptor.StudentAdapter;
import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.UI.classManagement.AddStudentActivity;
import com.cashpor.coachingcenter_app.model.Student;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private StudentAdapter adapter;
    private final List<Student> students = new ArrayList<>();

    private MaterialCheckBox cbAll;
    private MaterialButton btnDelete;

    // guard to avoid select-all listener firing when we update it programmatically
    private boolean updatingSelectAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        MaterialButton btnAddNew = findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(v ->
                startActivity(new Intent(StudentListActivity.this, AddStudentActivity.class))
        );

        cbAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);

        // Dummy data
        students.add(new Student(1, "Rajesh Ghawade", "Batch A", "CBSE", "English", "10", "2026-02-11 08:02"));
        students.add(new Student(2, "Subhash Ghule", "Batch B", "ICSE", "Hindi", "9", "2026-02-07 10:44"));
        students.add(new Student(3, "Aman More", "Batch A", "CBSE", "English", "8", "2026-02-03 12:04"));

        RecyclerView rv = findViewById(R.id.rvStudents);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StudentAdapter(students);
        rv.setAdapter(adapter);

        // ✅ Adapter callbacks
        adapter.setListener(new StudentAdapter.SelectionListener() {
            @Override
            public void onSelectionChanged() {
                refreshHeaderUI();
            }

            @Override
            public void onEdit(Student s, int position) {
                Toast.makeText(StudentListActivity.this, "Edit: " + s.name, Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(...));
            }

            @Override
            public void onDelete(Student s, int position) {
                students.remove(position);
                adapter.notifyItemRemoved(position);
                refreshHeaderUI();
            }
        });

        // ✅ Select All (only one listener)
        cbAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (updatingSelectAll) return; // ignore programmatic change

            for (Student s : students) s.selected = isChecked;
            adapter.notifyDataSetChanged();
            refreshHeaderUI();
        });

        // ✅ Delete selected button action
        btnDelete.setOnClickListener(v -> {
            int before = students.size();

            for (Iterator<Student> it = students.iterator(); it.hasNext();) {
                Student s = it.next();
                if (s.selected) it.remove();
            }

            int removed = before - students.size();
            if (removed == 0) {
                Toast.makeText(this, "No students selected", Toast.LENGTH_SHORT).show();
                return;
            }

            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Deleted " + removed + " students", Toast.LENGTH_SHORT).show();
            refreshHeaderUI();
        });

        // ✅ initial UI
        refreshHeaderUI();
    }

    private void refreshHeaderUI() {
        int count = adapter.getSelectedCount();
        btnDelete.setText("Delete " + count + " Selected");

        // update select all checkbox based on rows
        updatingSelectAll = true;
        cbAll.setChecked(adapter.areAllSelected());
        updatingSelectAll = false;
    }
}