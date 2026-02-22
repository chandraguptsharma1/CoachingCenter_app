package com.cashpor.coachingcenter_app.UI;

import android.content.Intent;
import android.os.Bundle;

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
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private StudentAdapter adapter;
    private final List<Student> students = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        MaterialButton btnAddNew = findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(v ->
                startActivity(new Intent(StudentListActivity.this, AddStudentActivity.class))
        );

        // Dummy data
        students.add(new Student(1, "Rajesh Ghawade", "Batch A", "CBSE", "English", "10", "2026-02-11 08:02"));
        students.add(new Student(2, "Subhash Ghule", "Batch B", "ICSE", "Hindi", "9", "2026-02-07 10:44"));
        students.add(new Student(3, "Aman More", "Batch A", "CBSE", "English", "8", "2026-02-03 12:04"));

        RecyclerView rv = findViewById(R.id.rvStudents);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(students);
        rv.setAdapter(adapter);

        // Select all
        MaterialCheckBox cbAll = findViewById(R.id.cbSelectAll);
        cbAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (Student s : students) s.selected = isChecked;
            adapter.notifyDataSetChanged();
        });
    }
}