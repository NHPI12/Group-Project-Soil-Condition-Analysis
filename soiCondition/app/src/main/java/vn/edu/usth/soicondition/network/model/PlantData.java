package vn.edu.usth.soicondition.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlantData implements Serializable {
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
}
