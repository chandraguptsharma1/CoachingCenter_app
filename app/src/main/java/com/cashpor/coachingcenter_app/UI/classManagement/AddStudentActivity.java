package com.cashpor.coachingcenter_app.UI.classManagement;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.google.android.material.button.MaterialButton;

public class AddStudentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        MaterialButton btn = findViewById(R.id.btnSubmit);
        btn.setOnClickListener(v -> Toast.makeText(this, "Submitted (dummy) ✅", Toast.LENGTH_SHORT).show());
    }
}