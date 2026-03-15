package com.cashpor.coachingcenter_app.UI.classManagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.model.*;
import com.cashpor.coachingcenter_app.network.ApiClient;
import com.cashpor.coachingcenter_app.network.ApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatchList extends AppCompatActivity {

    private TableLayout tlBatch;
    private MaterialCheckBox cbSelectAll;
    private MaterialButton btnDelete, btnAddNew;

    private final List<Batch> list = new ArrayList<>();
    private boolean updatingSelectAll = false;

    private ProgressDialog progressDialog;
    private TextView tvNoData;
    private List<Board> boards = new ArrayList<>();
    private List<Medium> mediums = new ArrayList<>();
    private List<ClassModel> classes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);

        tlBatch = findViewById(R.id.tlBatch);
        cbSelectAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);
        btnAddNew = findViewById(R.id.btnAddNew);

        tvNoData = findViewById(R.id.tvNoData);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        loadBatchList();

        btnAddNew.setOnClickListener(v -> openCreateBatchDialog());

        btnDelete.setOnClickListener(v -> {

            List<String> ids = new ArrayList<>();

            for (Batch b : list) {
                if (b.selected) {
                    ids.add(b.batch_id);
                }
            }

            if (ids.isEmpty()) {

                Toast.makeText(this,
                        "No batch selected",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Delete Batch")
                    .setMessage("Are you sure you want to delete selected batch?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        for (String id : ids) {
                            deleteBatch(id);
                        }

                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (updatingSelectAll) return;

            for (Batch b : list) {
                b.selected = isChecked;
            }

            renderTable();
        });
    }

    // ========================
    // LOAD BATCH LIST
    // ========================

    private void loadBatchList() {

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Login again", Toast.LENGTH_LONG).show();
            return;
        }

        showLoading();

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getBatches("Bearer " + token).enqueue(new Callback<BatchResponse>() {

            @Override
            public void onResponse(Call<BatchResponse> call, Response<BatchResponse> response) {

                hideLoading();

                if (response.body() == null || response.body().data == null) return;

                list.clear();
                list.addAll(response.body().data);

                renderTable();
            }

            @Override
            public void onFailure(Call<BatchResponse> call, Throwable t) {

                hideLoading();
                list.clear();
                renderTable();
                Toast.makeText(BatchList.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ========================
    // TABLE
    // ========================

    private void renderTable() {

        tlBatch.removeAllViews();

        if (list == null || list.isEmpty()) {

            tlBatch.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            return;
        }

        tlBatch.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);

        addHeaderRow();

        for (int i = 0; i < list.size(); i++) {
            addDataRow(i, list.get(i));
        }

        refreshHeaderUI();
    }

    private void refreshHeaderUI() {

        int selectedCount = 0;
        boolean all = !list.isEmpty();

        for (Batch b : list) {

            if (b.selected) selectedCount++;
            else all = false;
        }

        btnDelete.setText("Delete " + selectedCount + " Selected");

        updatingSelectAll = true;
        cbSelectAll.setChecked(all);
        updatingSelectAll = false;
    }

    private void addHeaderRow() {

        TableRow tr = new TableRow(this);

        tr.addView(headerCell(""));
        tr.addView(headerCell("SR.NO"));
        tr.addView(headerCell("NAME"));
        tr.addView(headerCell("BOARD"));
        tr.addView(headerCell("MEDIUM"));
        tr.addView(headerCell("CLASS"));
        tr.addView(headerCell("CREATED"));
        tr.addView(headerCell("ACTION"));

        tlBatch.addView(tr);
    }

    private void addDataRow(int index, Batch b) {

        TableRow tr = new TableRow(this);

        tr.addView(cbCell(b));
        tr.addView(textCell(String.valueOf(index + 1)));
        tr.addView(textCell(b.batch_name));
        tr.addView(textCell(b.batch_id));
        tr.addView(textCell(b.medium_id));
        tr.addView(textCell(b.class_id));
        tr.addView(textCell(b.created_on));

        TextView action = textCell("Delete");

        action.setTextColor(Color.RED);

        action.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Delete Batch")
                    .setMessage("Delete this batch?")
                    .setPositiveButton("Delete", (d, w) -> {
                        deleteBatch(b.batch_id);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        tr.addView(action);

        tlBatch.addView(tr);
    }

    private TextView headerCell(String text) {

        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(20,20,20,20);
        tv.setBackgroundResource(R.drawable.bg_table_header);

        return tv;
    }

    private TextView textCell(String text) {

        TextView tv = new TextView(this);
        tv.setText(text == null ? "" : text);
        tv.setPadding(20,15,20,15);
        tv.setBackgroundResource(R.drawable.bg_table_cell);

        return tv;
    }

    private CheckBox cbCell(Batch b) {

        CheckBox cb = new CheckBox(this);

        cb.setChecked(b.selected);

        cb.setOnCheckedChangeListener((btn, isChecked) -> {

            b.selected = isChecked;
            refreshHeaderUI();
        });

        return cb;
    }

    // ========================
    // CREATE BATCH
    // ========================

    private void openCreateBatchDialog() {

        View view = getLayoutInflater().inflate(R.layout.dialog_create_batch, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        EditText etBatchName = view.findViewById(R.id.etBatchName);

        Spinner spBoard = view.findViewById(R.id.spBoard);
        Spinner spMedium = view.findViewById(R.id.spMedium);
        Spinner spClass = view.findViewById(R.id.spClass);

        MaterialButton btnSave = view.findViewById(R.id.btnAddBatch);

        loadBoards(spBoard, spMedium, spClass);

        MaterialButton btnDialogClose = view.findViewById(R.id.btnDialogClose);
        TextView btnClose = view.findViewById(R.id.btnClose);

        // ❌ Close button
        btnDialogClose.setOnClickListener(v -> dialog.dismiss());

        // ❌ Cross icon
        btnClose.setOnClickListener(v -> dialog.dismiss());


        btnSave.setOnClickListener(v -> {

            String batchName = etBatchName.getText().toString().trim();

            if (batchName.isEmpty()) {
                Toast.makeText(this, "Enter batch name", Toast.LENGTH_SHORT).show();
                return;
            }

            int boardPos = spBoard.getSelectedItemPosition();
            int mediumPos = spMedium.getSelectedItemPosition();
            int classPos = spClass.getSelectedItemPosition();

            if (boardPos == 0 || mediumPos == 0 || classPos == 0) {
                Toast.makeText(this, "Select Board, Medium and Class", Toast.LENGTH_SHORT).show();
                return;
            }

            String boardId = boards.get(boardPos - 1).board_id;
            String mediumId = mediums.get(mediumPos - 1).medium_id;
            String classId = classes.get(classPos - 1).class_id;

            createBatch(batchName, boardId, mediumId, classId);

            dialog.dismiss();
        });
    }

    private void createBatch(String batchName, String boardId, String mediumId, String classId) {

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        AddBatchRequest request = new AddBatchRequest();
        request.batch_name = batchName;
        request.board_id = boardId;
        request.medium_id = mediumId;
        request.class_id = classId;
        request.branch_id = null;

        ApiService api = ApiClient.getClient().create(ApiService.class);

        showLoading();

        api.addBatch("Bearer " + token, request)
                .enqueue(new Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {

                        hideLoading();

                        if (response.body() != null && response.body().success) {

                            Toast.makeText(BatchList.this,
                                    "Batch Created Successfully",
                                    Toast.LENGTH_LONG).show();

                            loadBatchList(); // refresh table
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {

                        hideLoading();

                        Toast.makeText(BatchList.this,
                                "Failed : " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ========================
    // BOARD
    // ========================

    private void loadBoards(Spinner spBoard, Spinner spMedium, Spinner spClass) {

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getBoards("Bearer " + token).enqueue(new Callback<BoardResponse>() {

            @Override
            public void onResponse(Call<BoardResponse> call, Response<BoardResponse> response) {

                if (response.body() == null || response.body().data == null) return;

                boards = response.body().data;

                List<String> names = new ArrayList<>();
                names.add("Select Board");

                for (Board b : boards) {
                    names.add(b.board_name == null ? "Unknown Board" : b.board_name);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        BatchList.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        names
                );

                spBoard.setAdapter(adapter);

                spBoard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) return;

                        String boardId = boards.get(position - 1).board_id;

                        loadMediums(boardId, spMedium, spClass);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }

            @Override
            public void onFailure(Call<BoardResponse> call, Throwable t) {}
        });
    }

    // ========================
    // MEDIUM
    // ========================

    private void loadMediums(String boardId, Spinner spMedium, Spinner spClass) {

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getMediums("Bearer " + token, boardId)
                .enqueue(new Callback<MediumResponse>() {

                    @Override
                    public void onResponse(Call<MediumResponse> call, Response<MediumResponse> response) {

                        if (response.body() == null || response.body().data == null) return;

                        mediums = response.body().data;

                        List<String> names = new ArrayList<>();
                        names.add("Select Medium");

                        for (Medium m : mediums) {

                            if (m.medium != null) {
                                names.add(m.medium);
                            } else {
                                names.add("Unknown Medium");
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                BatchList.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                names
                        );

                        spMedium.setAdapter(adapter);

                        spMedium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) return;

                                String mediumId = mediums.get(position - 1).medium_id;

                                loadClasses(boardId, mediumId, spClass);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                    }

                    @Override
                    public void onFailure(Call<MediumResponse> call, Throwable t) {

                        Toast.makeText(BatchList.this, "Failed to load mediums", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ========================
    // CLASS
    // ========================

    private void deleteBatch(String batchId) {

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        ApiService api = ApiClient.getClient().create(ApiService.class);

        showLoading();

        api.deleteBatch("Bearer " + token, batchId)
                .enqueue(new Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {

                        hideLoading();

                        if (response.body() != null && response.body().success) {

                            Toast.makeText(BatchList.this,
                                    "Batch Deleted",
                                    Toast.LENGTH_SHORT).show();

                            loadBatchList(); // refresh list
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {

                        hideLoading();

                        Toast.makeText(BatchList.this,
                                "Delete Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadClasses(String boardId, String mediumId, Spinner spClass) {

        String token = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getString("token", "");

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getClasses("Bearer " + token, boardId, mediumId)
                .enqueue(new Callback<ClassResponse>() {

                    @Override
                    public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {

                        if (response.body() == null || response.body().data == null) return;

                        classes = response.body().data;

                        List<String> names = new ArrayList<>();
                        names.add("Select Class");

                        for (ClassModel c : classes) {
                            names.add(c.class_name == null ? "Unknown Class" : c.class_name);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                BatchList.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                names
                        );

                        spClass.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<ClassResponse> call, Throwable t) {}
                });
    }

    private void showLoading() {
        if (!progressDialog.isShowing()) progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }
}