package com.cashpor.coachingcenter_app.UI.classManagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateLecture extends AppCompatActivity {

    Spinner spBoard, spMedium, spClass, spSubject, spBatch, spChapter;
    EditText etDateTime, etDuration;
    MaterialButton btnSubmit;

    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lecture);

        spBoard = findViewById(R.id.spBoard);
        spMedium = findViewById(R.id.spMedium);
        spClass = findViewById(R.id.spClass);
        spSubject = findViewById(R.id.spSubject);
        spBatch = findViewById(R.id.spBatch);
        spChapter = findViewById(R.id.spChapter);

        etDateTime = findViewById(R.id.etDateTime);
        etDuration = findViewById(R.id.etDuration);
        btnSubmit = findViewById(R.id.btnSubmit);

        setSpinner(spBoard, new String[]{"--- Select Board ---", "MHSB", "CBSE", "ICSE"});
        setSpinner(spMedium, new String[]{"--- Select Medium ---", "English", "Semi English", "Hindi"});
        setSpinner(spClass, new String[]{"--- Select Class ---", "7th Std", "8th Std", "9th Std", "10th Std"});
        setSpinner(spSubject, new String[]{"--- Select Subject ---", "Maths", "Science", "History", "English"});
        setSpinner(spBatch, new String[]{"--- Select Batch ---", "Batch A", "Batch B", "Batch C"});
        setSpinner(spChapter, new String[]{"--- Select Chapter ---", "Ch-1", "Ch-2", "Ch-3"});

        etDateTime.setOnClickListener(v -> pickDateThenTime());

        btnSubmit.setOnClickListener(v -> {
            String dt = etDateTime.getText().toString().trim();
            String dur = etDuration.getText().toString().trim();

            if (spBoard.getSelectedItem().toString().startsWith("---") ||
                    spMedium.getSelectedItem().toString().startsWith("---") ||
                    spClass.getSelectedItem().toString().startsWith("---") ||
                    spSubject.getSelectedItem().toString().startsWith("---") ||
                    spBatch.getSelectedItem().toString().startsWith("---") ||
                    spChapter.getSelectedItem().toString().startsWith("---")) {
                Toast.makeText(this, "Please select all dropdown values", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dt.isEmpty()) {
                etDateTime.setError("Select date time");
                etDateTime.requestFocus();
                return;
            }
            if (dur.isEmpty()) {
                etDuration.setError("Enter duration");
                etDuration.requestFocus();
                return;
            }

            Toast.makeText(this, "Lecture Created ✅", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void pickDateThenTime() {
        DatePickerDialog dp = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog tp = new TimePickerDialog(
                            this,
                            (timeView, hourOfDay, minute) -> {
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                etDateTime.setText(fmt.format(cal.getTime()));
                            },
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            false
                    );
                    tp.show();
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dp.show();
    }

    private void setSpinner(Spinner sp, String[] items) {
        android.widget.ArrayAdapter<String> ad =
                new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sp.setAdapter(ad);
    }
}