package com.cashpor.coachingcenter_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cashpor.coachingcenter_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

        btnSignIn.setOnClickListener(v -> doDummyLogin());
    }

    private void showLoading(boolean show) {
        if (loadingOverlay != null) {
            loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        btnSignIn.setEnabled(!show);
        btnSignIn.setAlpha(show ? 0.7f : 1f);
        btnSignIn.setText(show ? "Signing in..." : "Sign in");
    }

    private void doDummyLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String pass  = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        tilEmail.setError(null);
        tilPassword.setError(null);

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email required");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            tilPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }

        // ✅ Show loader
        showLoading(true);

        // ✅ Fake API delay (1 second) for loader effect
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (email.equalsIgnoreCase(DUMMY_EMAIL) && pass.equals(DUMMY_PASS)) {

                getSharedPreferences("app_prefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("is_logged_in", true)
                        .apply();

                Toast.makeText(this, "Login Success ✅", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Login.this, home.class);
                startActivity(i);
                finish();

            } else {
                showLoading(false);
                tilPassword.setError("Invalid email or password");
                Toast.makeText(this, "Invalid credentials ❌", Toast.LENGTH_SHORT).show();
            }

        }, 1000);
    }
}