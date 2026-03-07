package com.cashpor.coachingcenter_app.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cashpor.coachingcenter_app.Adaptor.DashboardAdapter;
import com.cashpor.coachingcenter_app.R;

import com.cashpor.coachingcenter_app.UI.classManagement.BatchList;
import com.cashpor.coachingcenter_app.UI.classManagement.StudentListActivity;
import com.cashpor.coachingcenter_app.components.DashboardCard;
import com.cashpor.coachingcenter_app.components.Legend;
import com.google.android.material.navigation.NavigationView;
import com.cashpor.coachingcenter_app.UI.TestList;

import java.util.ArrayList;
import java.util.Arrays;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;


public class home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvHello, tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

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

        tvHello.setText("Hi, " + name);
        tvRole.setText(role + " (" + email + ")");

        View headerView = navigationView.getHeaderView(0);

        TextView tvHeaderName = headerView.findViewById(R.id.tvHeaderName);
        TextView tvHeaderStatus = headerView.findViewById(R.id.tvHeaderStatus);

        tvHeaderName.setText("Hi, " + name);
        tvHeaderStatus.setText(role);


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

        // ✅ Drawer item click
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_students) {
                startActivity(new Intent(home.this, StudentListActivity.class));
            } else if(id == R.id.nav_batch){
                startActivity(new Intent(home.this, BatchList.class));
            } else if (id == R.id.nav_test) {
                startActivity(new Intent(home.this, TestList.class));
            } else if (id == R.id.nav_paper_setting) {
//                startActivity(new Intent(home.this, PaperSettingActivity.class)); // if you made it
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}