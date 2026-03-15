package com.cashpor.coachingcenter_app.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.cashpor.coachingcenter_app.Adaptor.DashboardAdapter;

import com.cashpor.coachingcenter_app.R;
import com.cashpor.coachingcenter_app.UI.classManagement.AttendanceListActivity;
import com.cashpor.coachingcenter_app.UI.classManagement.BatchList;
import com.cashpor.coachingcenter_app.UI.classManagement.StudentListActivity;

import com.cashpor.coachingcenter_app.UI.classManagement.TestList;
import com.cashpor.coachingcenter_app.components.DashboardCard;
import com.cashpor.coachingcenter_app.components.Legend;
import com.cashpor.coachingcenter_app.helper.AppUpdateHelper;
import com.cashpor.coachingcenter_app.model.AppVersionData;
import com.cashpor.coachingcenter_app.model.AppVersionResponse;
import com.cashpor.coachingcenter_app.model.PaperSettingData;
import com.cashpor.coachingcenter_app.model.PaperSettingResponse;
import com.cashpor.coachingcenter_app.network.ApiClient;
import com.cashpor.coachingcenter_app.network.ApiService;
import com.google.android.material.navigation.NavigationView;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvHello, tvRole;
    private ImageView ivAvatar, ivHeaderLogo;

    private ProgressBar pbAvatar, pbHeaderAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        ivAvatar = findViewById(R.id.ivAvatar);



        findViewById(R.id.btnMenu).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        RecyclerView rv = findViewById(R.id.rvDashboard);
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        ArrayList<DashboardCard> cards = new ArrayList<>();

        tvHello = findViewById(R.id.tvHello);
        tvRole = findViewById(R.id.tvRole);

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        String name = prefs.getString("user_name", "User");
        String role = prefs.getString("user_role", "");
        String email = prefs.getString("user_email", "");
        String token = prefs.getString("token", "");

        tvHello.setText("Hi, " + name);
        tvRole.setText(role + " (" + email + ")");

        View headerView = navigationView.getHeaderView(0);

        TextView tvHeaderName = headerView.findViewById(R.id.tvHeaderName);
        TextView tvHeaderStatus = headerView.findViewById(R.id.tvHeaderStatus);
        ivHeaderLogo = headerView.findViewById(R.id.ivHeaderAvatar);
        pbAvatar = findViewById(R.id.pbAvatar);
        pbHeaderAvatar = headerView.findViewById(R.id.pbHeaderAvatar);

        tvHeaderName.setText("Hi, " + name);
        tvHeaderStatus.setText(role);

        Glide.with(this)
                .load(R.drawable.ic_avatar_placeholder)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .circleCrop()
                .into(ivAvatar);

        Glide.with(this)
                .load(R.drawable.ic_avatar_placeholder)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .circleCrop()
                .into(ivHeaderLogo);

        if (token != null && !token.isEmpty()) {
            loadPaperSettingLogo(token);
        }

        checkAppUpdate();

        cards.add(new DashboardCard("Lecture",
                Arrays.asList(
                        new Legend("LECTURE SCHEDULE : 0", "#F59E0B"),
                        new Legend("LECTURE COMPLETED : 0", "#22C55E"),
                        new Legend("LECTURE CANCEL : 0", "#3B82F6")
                )));

        cards.add(new DashboardCard("Test",
                Arrays.asList(
                        new Legend("MCQ TEST : 0", "#F59E0B"),
                        new Legend("OBJECTIVE TEST : 0", "#22C55E"),
                        new Legend("SUBJECTIVE TEST : 3", "#3B82F6")
                )));

        cards.add(new DashboardCard("Upcoming Test",
                Arrays.asList(
                        new Legend("MCQ TEST : 0", "#F59E0B"),
                        new Legend("OBJECTIVE TEST : 0", "#22C55E"),
                        new Legend("SUBJECTIVE TEST : 0", "#3B82F6")
                )));

        cards.add(new DashboardCard("Student",
                Arrays.asList(
                        new Legend("TOTAL STUDENT : 0", "#3B82F6")
                )));

        rv.setAdapter(new DashboardAdapter(cards));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_students) {
                startActivity(new Intent(home.this, StudentListActivity.class));
            } else if (id == R.id.nav_batch) {
                startActivity(new Intent(home.this, BatchList.class));
            } else if (id == R.id.nav_attendance) {
                startActivity(new Intent(home.this, AttendanceListActivity.class));
            } else if (id == R.id.nav_test) {
                 startActivity(new Intent(home.this, TestList.class));
            } else if (id == R.id.nav_paper_setting) {
                // startActivity(new Intent(home.this, PaperSettingActivity.class));
            }else if (id == R.id.nav_logout) {
                logoutUser();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void logoutUser() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();

                    Toast.makeText(home.this, "Logout successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(home.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void loadPaperSettingLogo(String token) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        pbAvatar.setVisibility(View.VISIBLE);
        pbHeaderAvatar.setVisibility(View.VISIBLE);

        Call<PaperSettingResponse> call = apiService.getMyPaperSetting("Bearer " + token);
        call.enqueue(new Callback<PaperSettingResponse>() {
            @Override
            public void onResponse(Call<PaperSettingResponse> call, Response<PaperSettingResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    pbAvatar.setVisibility(View.GONE);
                    pbHeaderAvatar.setVisibility(View.GONE);
                    Toast.makeText(home.this, "Paper setting fetch failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                PaperSettingResponse body = response.body();
                PaperSettingData data = body.getData();

                if (data == null || data.getLogoUrl() == null || data.getLogoUrl().isEmpty()) {
                    pbAvatar.setVisibility(View.GONE);
                    pbHeaderAvatar.setVisibility(View.GONE);
                    return;
                }

                String logoUrl = data.getLogoUrl();

                Glide.with(home.this)
                        .load(logoUrl)
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .error(R.drawable.ic_avatar_placeholder)
                        .circleCrop()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                pbAvatar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                pbAvatar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(ivAvatar);

                Glide.with(home.this)
                        .load(logoUrl)
                        .placeholder(R.drawable.ic_avatar_placeholder)
                        .error(R.drawable.ic_avatar_placeholder)
                        .circleCrop()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                pbHeaderAvatar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                pbHeaderAvatar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(ivHeaderLogo);
            }

            @Override
            public void onFailure(Call<PaperSettingResponse> call, Throwable t) {
                pbAvatar.setVisibility(View.GONE);
                pbHeaderAvatar.setVisibility(View.GONE);
                Toast.makeText(home.this, "Logo load failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAppUpdate() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<AppVersionResponse> call = apiService.getAppVersion("coaching_center");
        call.enqueue(new Callback<AppVersionResponse>() {
            @Override
            public void onResponse(Call<AppVersionResponse> call, Response<AppVersionResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    return;
                }

                AppVersionData data = response.body().getData();

                int currentVersionCode = 2;
                int latestVersionCode = data.getVersionCode();

                if (AppUpdateHelper.isUpdateAvailable(currentVersionCode, latestVersionCode)) {
                    AppUpdateHelper.showUpdateDialog(home.this, data);
                }
            }

            @Override
            public void onFailure(Call<AppVersionResponse> call, Throwable t) {
                // update check fail silently
            }
        });
    }
}