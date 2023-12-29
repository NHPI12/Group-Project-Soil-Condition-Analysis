package vn.edu.usth.soicondition.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class default_Image implements Parcelable {
    @SerializedName("license")
    private int license;
    @SerializedName("license_name")
    private String license_name;
    @SerializedName("license_url")
    private String license_url;
    @SerializedName("original_url")
    private String original_url;
    @SerializedName("regular_url")
    private String regular_url;
    @SerializedName("medium_url")
    private String medium_url;
    @SerializedName("small_url")
    private String small_url;
    @SerializedName("thumbnail")
    private String thumbnail;

    protected default_Image(Parcel in) {
        license = in.readInt();
        license_name = in.readString();
        license_url = in.readString();
        original_url = in.readString();
        regular_url = in.readString();
        medium_url = in.readString();
        small_url = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<default_Image> CREATOR = new Creator<default_Image>() {
        @Override
        public default_Image createFromParcel(Parcel in) {
            return new default_Image(in);
        }

        @Override
        public default_Image[] newArray(int size) {
            return new default_Image[size];
        }
    };

    public int getLicense() {
        return license;
    }

    public void setLicense(int license) {
        this.license = license;
    }

    public String getLicenseName() {
        return license_name;
    }

    public void setLicenseName(String licenseName) {
        this.license_name = licenseName;
    }

    public String getLicenseUrl() {
        return license_url;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.license_url = licenseUrl;
    }

    public String getMediumUrl() {
        return medium_url;
    }

    public void setMediumUrl(String mediumUrl) {
        this.medium_url = mediumUrl;
    }

    public String getOriginalUrl() {
        return original_url;
    }

    public void setOriginalUrl(String originalUrl) {
        this.original_url = originalUrl;
    }

    public String getRegularUrl() {
        return regular_url;
    }

    public void setRegularUrl(String regularUrl) {
        this.regular_url = regularUrl;
    }

    public String getSmallUrl() {
        return small_url;
    }

    public void setSmallUrl(String smallUrl) {
        this.small_url = smallUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(license);
        dest.writeString(license_name);
        dest.writeString(license_url);
        dest.writeString(original_url);
        dest.writeString(regular_url);
        dest.writeString(medium_url);
        dest.writeString(small_url);
        dest.writeString(thumbnail);
    }
}
