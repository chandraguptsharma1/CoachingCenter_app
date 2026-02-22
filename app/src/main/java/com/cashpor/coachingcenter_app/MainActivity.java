package com.cashpor.coachingcenter_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cashpor.coachingcenter_app.UI.Login;

public class MainActivity extends AppCompatActivity {

    private View card;
    private View logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        card = findViewById(R.id.card);
        logo = findViewById(R.id.ivLogo);

        // Start logo animation
        Animation pop = AnimationUtils.loadAnimation(this, R.anim.splash_pop);
        logo.startAnimation(pop);

        // Optional: slow pulse on circles (simple code animation)
        animateCircle(R.id.circle1, 1800, 1.00f, 1.04f);
        animateCircle(R.id.circle2, 2000, 1.00f, 1.06f);
        animateCircle(R.id.circle3, 2200, 1.00f, 1.08f);

        // After 1.6 sec -> exit + go next
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Animation exit = AnimationUtils.loadAnimation(this, R.anim.splash_exit);
            card.startAnimation(exit);

            // when exit finished -> open next activity
            exit.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    // CHANGE HomeActivity to your next screen activity
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                @Override public void onAnimationRepeat(Animation animation) {}
            });

        }, 3000);
    }

    private void animateCircle(int viewId, long duration, float from, float to) {
        View v = findViewById(viewId);
        if (v == null) return;

        v.setScaleX(from);
        v.setScaleY(from);

        v.animate()
                .scaleX(to)
                .scaleY(to)
                .setDuration(duration)
                .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                .setStartDelay(150)
                .withEndAction(() -> v.animate()
                        .scaleX(from)
                        .scaleY(from)
                        .setDuration(duration)
                        .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                        .start())
                .start();
    }
}