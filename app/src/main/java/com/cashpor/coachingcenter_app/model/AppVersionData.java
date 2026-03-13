package com.cashpor.coachingcenter_app.model;

import com.google.gson.annotations.SerializedName;

public class AppVersionData {

    @SerializedName("id")
    private int id;

    @SerializedName("app_name")
    private String appName;

    @SerializedName("latest_version")
    private String latestVersion;

    @SerializedName("version_code")
    private int versionCode;

    @SerializedName("apk_url")
    private String apkUrl;

    @SerializedName("force_update")
    private int forceUpdate;

    @SerializedName("release_notes")
    private String releaseNotes;

    @SerializedName("status")
    private int status;

    public int getId() {
        return id;
    }

    public String getAppName() {
        return appName;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public int getStatus() {
        return status;
    }
}