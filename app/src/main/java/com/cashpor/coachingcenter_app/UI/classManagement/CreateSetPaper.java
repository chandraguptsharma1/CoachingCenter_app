package com.cashpor.coachingcenter_app.UI.classManagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CreateSetPaper extends AppCompatActivity {

    Spinner spBoard, spMedium, spClass, spSubject, spContentType;
    EditText etDate, etMarks, etPaperTime;
    ListView lvAll, lvSelected;
    TextView tvSelectAllLeft, tvDeselectAllRight;
    MaterialButton btnProcess;

    ArrayList<String> allChapters = new ArrayList<>();
    ArrayList<String> selectedChapters = new ArrayList<>();
    ArrayAdapter<String> adAll, adSel;

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_set_paper);

        spBoard = findViewById(R.id.spBoard);
        spMedium = findViewById(R.id.spMedium);
        spClass = findViewById(R.id.spClass);
        spSubject = findViewById(R.id.spSubject);
        spContentType = findViewById(R.id.spContentType);

        etDate = findViewById(R.id.etDate);
        etMarks = findViewById(R.id.etMarks);
        etPaperTime = findViewById(R.id.etPaperTime);

        lvAll = findViewById(R.id.lvAll);
        lvSelected = findViewById(R.id.lvSelected);
        tvSelectAllLeft = findViewById(R.id.tvSelectAllLeft);
        tvDeselectAllRight = findViewById(R.id.tvDeselectAllRight);
        btnProcess = findViewById(R.id.btnProcess);

        // dropdown dummy
        setSpinner(spBoard, new String[]{"--- Select Board ---", "MHSB", "CBSE", "ICSE"});
        setSpinner(spMedium, new String[]{"--- Select Medium ---", "English", "Semi English", "Hindi"});
        setSpinner(spClass, new String[]{"--- Select Class ---", "7th Std", "8th Std", "9th Std", "10th Std"});
        setSpinner(spSubject, new String[]{"--- Select Subject ---", "Maths", "Science", "History", "English"});
        setSpinner(spContentType, new String[]{"Textual", "Objective", "Mixed"});

        // chapters dummy
        allChapters.add("Chapter 1");
        allChapters.add("Chapter 2");
        allChapters.add("Chapter 3");
        allChapters.add("Chapter 4");

        adAll = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, allChapters);
        lvAll.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvAll.setAdapter(adAll);

        adSel = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedChapters);
        lvSelected.setAdapter(adSel);

        // Date picker
        etDate.setOnClickListener(v -> {
            DatePickerDialog dp = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(df.format(cal.getTime()));
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            dp.show();
        });

        // click chapter add/remove
        lvAll.setOnItemClickListener((parent, view, position, id) -> {
            String ch = allChapters.get(position);
            boolean checked = lvAll.isItemChecked(position);
            if (checked) {
                if (!selectedChapters.contains(ch)) selectedChapters.add(ch);
            } else {
                selectedChapters.remove(ch);
            }
            adSel.notifyDataSetChanged();
        });

        // Select all / Deselect all
        tvSelectAllLeft.setOnClickListener(v -> {
            selectedChapters.clear();
            for (int i = 0; i < allChapters.size(); i++) {
                lvAll.setItemChecked(i, true);
                selectedChapters.add(allChapters.get(i));
            }
            adSel.notifyDataSetChanged();
        });

        tvDeselectAllRight.setOnClickListener(v -> {
            selectedChapters.clear();
            for (int i = 0; i < allChapters.size(); i++) lvAll.setItemChecked(i, false);
            adSel.notifyDataSetChanged();
        });

        // Data Process
        btnProcess.setOnClickListener(v -> {
            if (spBoard.getSelectedItem().toString().startsWith("---") ||
                    spMedium.getSelectedItem().toString().startsWith("---") ||
                    spClass.getSelectedItem().toString().startsWith("---") ||
                    spSubject.getSelectedItem().toString().startsWith("---")) {
                Toast.makeText(this, "Select Board/Medium/Class/Subject", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etDate.getText().toString().trim().isEmpty()) {
                etDate.setError("Select Date");
                etDate.requestFocus();
                return;
            }
            if (etMarks.getText().toString().trim().isEmpty()) {
                etMarks.setError("Enter Marks");
                etMarks.requestFocus();
                return;
            }
            if (etPaperTime.getText().toString().trim().isEmpty()) {
                etPaperTime.setError("Enter Paper time");
                etPaperTime.requestFocus();
                return;
            }
            if (selectedChapters.isEmpty()) {
                Toast.makeText(this, "Select at least 1 chapter", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: API call
            Toast.makeText(this, "Data Process Done ✅", Toast.LENGTH_SHORT).show();
        });
    }

    private void setSpinner(Spinner sp, String[] items) {
        ArrayAdapter<String> ad =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sp.setAdapter(ad);
    }
}