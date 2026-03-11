package com.cashpor.coachingcenter_app.UI.classManagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.network.ApiClient;
import com.cashpor.coachingcenter_app.network.ApiService;
import com.cashpor.coachingcenter_app.network.model.AddStudentRequest;
import com.cashpor.coachingcenter_app.network.model.CommonResponse;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStudentActivity extends AppCompatActivity {

    private Spinner spBoard, spMedium, spClass, spBatch;
    private EditText etStudentName, etParentName, etAddress, etPhone, etParentPhone, etWhatsapp;
    private MaterialButton btnSubmit;

    // फिलहाल static IDs use kar rahe hain
    private final String[] boardNames = {"Select Board", "Board 1"};
    private final String[] boardIds   = {"", "BOARD_33E77AEF9"};

    private final String[] mediumNames = {"Select Medium", "Hindi"};
    private final String[] mediumIds   = {"", "MED_DB20C156D75"};

    private final String[] classNames = {"Select Class", "Class 10"};
    private final String[] classIds   = {"", "CLASS_DCFDA25CE"};

    private final String[] batchNames = {"Select Batch", "Morning Batch"};
    private final String[] batchIds   = {"", "BATCH_WGMTQDZD"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        initViews();
        setSpinnerData();

        btnSubmit.setOnClickListener(v -> submitStudent());
    }

    private void initViews() {
        spBoard = findViewById(R.id.spBoard);
        spMedium = findViewById(R.id.spMedium);
        spClass = findViewById(R.id.spClass);
        spBatch = findViewById(R.id.spBatch);

        etStudentName = findViewById(R.id.etStudentName);
        etParentName = findViewById(R.id.etParentName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etParentPhone = findViewById(R.id.etParentPhone);
        etWhatsapp = findViewById(R.id.etWhatsapp);

        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void setSpinnerData() {
        ArrayAdapter<String> boardAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, boardNames);
        boardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBoard.setAdapter(boardAdapter);

        ArrayAdapter<String> mediumAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mediumNames);
        mediumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMedium.setAdapter(mediumAdapter);

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, classNames);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(classAdapter);

        ArrayAdapter<String> batchAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, batchNames);
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBatch.setAdapter(batchAdapter);
    }

    private void setLoading(boolean loading) {
        btnSubmit.setEnabled(!loading);
        btnSubmit.setText(loading ? "Submitting..." : "Submit");
        btnSubmit.setAlpha(loading ? 0.7f : 1f);
    }

    private void submitStudent() {
        String boardId = boardIds[spBoard.getSelectedItemPosition()];
        String mediumId = mediumIds[spMedium.getSelectedItemPosition()];
        String classId = classIds[spClass.getSelectedItemPosition()];
        String batchId = batchIds[spBatch.getSelectedItemPosition()];

        String studentName = etStudentName.getText().toString().trim();
        String parentName = etParentName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String parentPhone = etParentPhone.getText().toString().trim();
        String whatsappPhone = etWhatsapp.getText().toString().trim();

        if (TextUtils.isEmpty(boardId)) {
            Toast.makeText(this, "Please select board", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mediumId)) {
            Toast.makeText(this, "Please select medium", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(classId)) {
            Toast.makeText(this, "Please select class", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(batchId)) {
            Toast.makeText(this, "Please select batch", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(studentName)) {
            etStudentName.setError("Student name required");
            etStudentName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(parentName)) {
            etParentName.setError("Parent name required");
            etParentName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Address required");
            etAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone required");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(parentPhone)) {
            etParentPhone.setError("Parent phone required");
            etParentPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(whatsappPhone)) {
            etWhatsapp.setError("Whatsapp phone required");
            etWhatsapp.requestFocus();
            return;
        }

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "User token not found. Please login again.", Toast.LENGTH_LONG).show();
            return;
        }

        setLoading(true);

        AddStudentRequest request = new AddStudentRequest(
                boardId,
                mediumId,
                classId,
                batchId,
                studentName,
                parentName,
                address,
                phone,
                parentPhone,
                whatsappPhone,
                parentPhone // parent_whatsapp_phone फिलहाल parent phone se bhej raha hoon
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<CommonResponse> call = apiService.addStudent("Bearer " + token, request);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    CommonResponse res = response.body();

                    if (res.success) {
                        Toast.makeText(AddStudentActivity.this,
                                res.message != null ? res.message : "Student added successfully",
                                Toast.LENGTH_LONG).show();
                        clearForm();
                    } else {
                        Toast.makeText(AddStudentActivity.this,
                                res.message != null ? res.message : "Failed to add student",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AddStudentActivity.this,
                            "Server error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(AddStudentActivity.this,
                        "API Failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearForm() {
        spBoard.setSelection(0);
        spMedium.setSelection(0);
        spClass.setSelection(0);
        spBatch.setSelection(0);

        etStudentName.setText("");
        etParentName.setText("");
        etAddress.setText("");
        etPhone.setText("");
        etParentPhone.setText("");
        etWhatsapp.setText("");
    }
}