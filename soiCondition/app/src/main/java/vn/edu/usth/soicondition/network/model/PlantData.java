package vn.edu.usth.soicondition.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlantData implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("common_name")
    private String common_name;
    public static final String SCIENTIFIC_NAME = "scientific_name";
    @SerializedName(SCIENTIFIC_NAME)
    private List<String> scientific_name;
    public static final String OTHER_NAME = "other_name";
    @SerializedName(OTHER_NAME)
    private List<String> other_name;
    @SerializedName("cycle")
    private String cycle;
    @SerializedName("watering")
    private String watering;
    public static final String SUNLIGHT_FIELD_NAME = "sunlight";
    @SerializedName(SUNLIGHT_FIELD_NAME)
    private List<String> sunlight;
    @SerializedName("default_image")
    private default_Image defaultImage;
    private boolean isChecked;

    protected PlantData(Parcel in) {
        id = in.readInt();
        common_name = in.readString();
        scientific_name = in.createStringArrayList();
        other_name = in.createStringArrayList();
        cycle = in.readString();
        watering = in.readString();
        sunlight = in.createStringArrayList();
        defaultImage = in.readParcelable(default_Image.class.getClassLoader());
        this.isChecked = false;
    }

    public static final Creator<PlantData> CREATOR = new Creator<PlantData>() {
        @Override
        public PlantData createFromParcel(Parcel in) {
            return new PlantData(in);
        }

        @Override
        public PlantData[] newArray(int size) {
            return new PlantData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getOther_name() {
        return other_name;
    }

    public void setOther_name(List<String> other_name) {
        this.other_name = other_name;
    }

    public List<String> getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(List<String> scientific_name) {
        this.scientific_name = scientific_name;
    }

    public List<String> getSunlight() {
        return sunlight;
    }

    public void setSunlight(List<String> sunlight) {
        this.sunlight = sunlight;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public default_Image getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(default_Image defaultImage) {
        this.defaultImage = defaultImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(common_name);
        dest.writeStringList(scientific_name);
        dest.writeStringList(other_name);
        dest.writeString(cycle);
        dest.writeString(watering);
        dest.writeStringList(sunlight);
        dest.writeParcelable(defaultImage, flags);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int convertWateringToValue() {
        String wateringInfo = getWatering();
        switch (wateringInfo) {
            case "Frequent":
                return 75;
            case "Average":
                return 68;
            case "Minimum":
                return 60;
            case "None":
                return 40;
            default:
                return 0; // Handle the default case or invalid values as needed
        }
    }

    public int convertSunlightToValue() {
        List<String> sunlightInfo = getSunlight();
        if (sunlightInfo.isEmpty()) {
            return 0;
        }

        int total = 0;
        int count = 0;
        int smallestFullShade = Integer.MAX_VALUE;
        int largestFullSun = Integer.MIN_VALUE;

        for (String sunlight : sunlightInfo) {
            switch (sunlight) {
                case "full shade":
                    smallestFullShade = Math.min(smallestFullShade, 15);
                    break;
                case "part shade":
                case "part sun/part shade":
                    total += (18 + 27) / 2;
                    count++;
                    break;
                case "filtered shade":
                    total += (16 + 25) / 2;
                    count++;
                    break;
                case "full sun":
                    largestFullSun = Math.max(largestFullSun, 33);
                    break;
                default:
                    // Handle unknown sunlight types or add more cases as needed
                    break;
            }
        }

        if (smallestFullShade != Integer.MAX_VALUE && largestFullSun != Integer.MIN_VALUE) {
            total += (smallestFullShade + largestFullSun) / 2;
            count++;
        }

        return count > 0 ? total / count : 0;
    }
}
