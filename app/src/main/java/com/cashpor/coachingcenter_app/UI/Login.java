package com.cashpor.coachingcenter_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

    private static final String DUMMY_EMAIL = "admin@gmail.com";
    private static final String DUMMY_PASS  = "123456";

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnSignIn;

    private View loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        loadingOverlay = findViewById(R.id.loadingOverlay);

        btnSignIn.setOnClickListener(v -> doLogin());
    }

    private void showLoading(boolean show) {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        btnSignIn.setEnabled(!show);
        btnSignIn.setAlpha(show ? 0.7f : 1f);
        btnSignIn.setText(show ? "Signing in..." : "Sign in");
    }

    private void doLogin() {

        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

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

// 🔹 URL log
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

                    if (res.status == 200) {

                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                                .edit()
                                .putString("token", res.token)
                                .putString("user_id", res.user.id)
                                .putString("user_name", res.user.name)
                                .putString("user_email", res.user.email)
                                .putString("user_role", res.user.role)
                                .putInt("user_roleId", res.user.roleId)
                                .putBoolean("is_logged_in", true)
                                .apply();

                        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(Login.this, home.class));
                        finish();

                    } else {
                        Toast.makeText(Login.this, res.message, Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Log.e("LOGIN_API", "Body null or response error");

                    try {
                        Log.e("LOGIN_API_ERROR", response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                showLoading(false);
                Log.i("Server Error: ",t.getMessage());
                Toast.makeText(Login.this, "Server Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}