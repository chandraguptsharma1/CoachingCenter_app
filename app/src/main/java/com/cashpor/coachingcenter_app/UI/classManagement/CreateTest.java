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

public class CreateTest extends AppCompatActivity {

    Spinner spBoard, spMedium, spClass, spSubject, spBatch, spType;
    EditText etMarks, etDateTime;
    MaterialButton btnSubmit;

    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);

        spBoard = findViewById(R.id.spBoard);
        spMedium = findViewById(R.id.spMedium);
        spClass = findViewById(R.id.spClass);
        spSubject = findViewById(R.id.spSubject);
        spBatch = findViewById(R.id.spBatch);
        spType = findViewById(R.id.spType);

        etMarks = findViewById(R.id.etMarks);
        etDateTime = findViewById(R.id.etDateTime);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Dummy dropdown data (later API)
        setSpinner(spBoard, new String[]{"--- Select Board ---", "MHSB", "CBSE", "ICSE"});
        setSpinner(spMedium, new String[]{"--- Select Medium ---", "English", "Semi English", "Hindi"});
        setSpinner(spClass, new String[]{"--- Select Class ---", "7th Std", "8th Std", "9th Std", "10th Std"});
        setSpinner(spSubject, new String[]{"--- Select Subject ---", "Maths", "Science", "History", "English"});
        setSpinner(spBatch, new String[]{"--- Select Batch ---", "Batch A", "Batch B", "Batch C"});
        setSpinner(spType, new String[]{"--- Select Question Paper Type ---", "Subjective", "Objective", "Mixed"});

        etDateTime.setOnClickListener(v -> pickDateThenTime());

        btnSubmit.setOnClickListener(v -> {
            String board = spBoard.getSelectedItem().toString();
            String medium = spMedium.getSelectedItem().toString();
            String cls = spClass.getSelectedItem().toString();
            String subject = spSubject.getSelectedItem().toString();
            String batch = spBatch.getSelectedItem().toString();
            String type = spType.getSelectedItem().toString();
            String marks = etMarks.getText().toString().trim();
            String dt = etDateTime.getText().toString().trim();

            if (board.startsWith("---") || medium.startsWith("---") || cls.startsWith("---")
                    || subject.startsWith("---") || batch.startsWith("---") || type.startsWith("---")) {
                Toast.makeText(this, "Please select all dropdown values", Toast.LENGTH_SHORT).show();
                return;
            }
            if (marks.isEmpty()) {
                etMarks.setError("Enter marks");
                etMarks.requestFocus();
                return;
            }
            if (dt.isEmpty()) {
                etDateTime.setError("Select date time");
                etDateTime.requestFocus();
                return;
            }

            // TODO: API call (create test)
            Toast.makeText(this, "Test Created ✅", Toast.LENGTH_SHORT).show();
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