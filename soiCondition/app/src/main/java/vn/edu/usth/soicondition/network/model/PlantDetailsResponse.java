package vn.edu.usth.soicondition.network.model;

import com.google.gson.annotations.SerializedName;

public class    PlantDetailsResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("common_name")
    private String commonName;
    public static final String WATERING_PERIOD = "watering_period";
    @SerializedName(WATERING_PERIOD)
    private String watering_period;
    @SerializedName("flowering_season")
    private String flowering_season;
    @SerializedName("description")
    private String description;
    @SerializedName("care_level")
    private String care_level;

    public void setId(int id) {
        this.id = id;
    }
    public void setCare_level(String care_level){ this.care_level = care_level;}
    public String getCare_level(){
        return care_level;
    }
    public int getId() {
        return id;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getDescription() {
        return description;
    }

    public String getFlowering_season() {
        return flowering_season;
    }

    public String getWatering_period() {
        return watering_period;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFlowering_season(String flowering_season) {
        this.flowering_season = flowering_season;
    }

    public void setWatering_period(String watering_period) {
        this.watering_period = watering_period;
    }
}
