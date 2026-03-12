package com.cashpor.coachingcenter_app.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.network.ApiClient;
import com.cashpor.coachingcenter_app.network.ApiService;
import com.cashpor.coachingcenter_app.network.model.LoginRequest;
import com.cashpor.coachingcenter_app.network.model.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnSignIn;
    private LinearLayout btnLoadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isUserLoggedIn()) {
            openHomeAndFinish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnLoadingLayout = findViewById(R.id.btnLoadingLayout);

        btnSignIn.setOnClickListener(v -> doLogin());
    }

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        return isLoggedIn && !TextUtils.isEmpty(token);
    }

    private void openHomeAndFinish() {
        startActivity(new Intent(Login.this, home.class));
        finish();
    }

    private void showLoading(boolean show) {
        btnSignIn.setEnabled(!show);
        btnSignIn.setClickable(!show);
        btnSignIn.setText(show ? "" : "Sign in");
        btnSignIn.setAlpha(show ? 0.9f : 1f);

        if (btnLoadingLayout != null) {
            btnLoadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
            btnLoadingLayout.bringToFront();
        }
    }

    private void doLogin() {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String pass = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email required");
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            tilPassword.setError("Password required");
            return;
        }

        showLoading(true);

        LoginRequest request = new LoginRequest(email, pass, "");
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(request);

        Log.d("API_URL", call.request().url().toString());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                Log.d("LOGIN_API", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();

                    Log.d("LOGIN_API", "Message: " + res.message);
                    Log.d("LOGIN_API", "Token: " + res.token);
                    Log.d("LOGIN_API", "Success: " + res.success);

                    if (res.success && res.user != null) {
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                                .edit()
                                .putString("token", res.token)
                                .putInt("user_id", res.user.id)
                                .putString("user_name", res.user.empName != null ? res.user.empName : "")
                                .putString("user_email", res.user.email != null ? res.user.email : "")
                                .putString("user_role", res.user.role != null ? res.user.role : "")
                                .putInt("user_roleId", res.user.roleId)
                                .putString("user_status", res.user.status != null ? res.user.status : "")
                                .putBoolean("is_logged_in", true)
                                .apply();

                        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                        openHomeAndFinish();
                    } else {
                        Toast.makeText(
                                Login.this,
                                res.message != null ? res.message : "Login failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {
                    try {
                        if (response.errorBody() != null) {
                            Log.e("LOGIN_API_ERROR", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Log.e("LOGIN_API", "Server Error: " + t.getMessage(), t);
                Toast.makeText(Login.this, "Server Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}