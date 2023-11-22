package vn.edu.usth.soicondition.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class default_Image implements Serializable {
    @SerializedName("license")
    private int license;
    @SerializedName("licenseName")
    private String licenseName;
    @SerializedName("licenseUrl")
    private String licenseUrl;
    @SerializedName("originalUrl")
    private String originalUrl;
    @SerializedName("regularUrl")
    private String regularUrl;
    @SerializedName("mediumUrl")
    private String mediumUrl;
    @SerializedName("smallUrl")
    private String smallUrl;
    @SerializedName("thumbnail")
    private String thumbnail;

    public int getLicense() {
        return license;
    }

    public void setLicense(int license) {
        this.license = license;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getRegularUrl() {
        return regularUrl;
    }

    public void setRegularUrl(String regularUrl) {
        this.regularUrl = regularUrl;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
