package com.cashpor.coachingcenter_app.components;

import java.util.List;

public class DashboardCard {
    public String title;
    public List<Legend> legends;

    public DashboardCard(String title, List<Legend> legends) {
        this.title = title;
        this.legends = legends;
    }
}
