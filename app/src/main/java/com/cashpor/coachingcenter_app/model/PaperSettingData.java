package com.cashpor.coachingcenter_app.model;


import com.google.gson.annotations.SerializedName;

public class PaperSettingData {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("logo_file")
    private String logoFile;

    @SerializedName("title")
    private String title;

    @SerializedName("watermark_title")
    private String watermarkTitle;

    @SerializedName("watermark_logo")
    private String watermarkLogo;

    @SerializedName("watermark_type")
    private String watermarkType;

    @SerializedName("format_type")
    private int formatType;

    @SerializedName("website")
    private String website;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("logo_url")
    private String logoUrl;

    @SerializedName("watermark_logo_url")
    private String watermarkLogoUrl;

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getLogoFile() {
        return logoFile;
    }

    public String getTitle() {
        return title;
    }

    public String getWatermarkTitle() {
        return watermarkTitle;
    }

    public String getWatermarkLogo() {
        return watermarkLogo;
    }

    public String getWatermarkType() {
        return watermarkType;
    }

    public int getFormatType() {
        return formatType;
    }

    public String getWebsite() {
        return website;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getWatermarkLogoUrl() {
        return watermarkLogoUrl;
    }
}
