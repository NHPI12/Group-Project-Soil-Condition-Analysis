package vn.edu.usth.soicondition.network.model;

import com.google.gson.annotations.SerializedName;

public class    PlantDetailsResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("common_name")
    private String commonName;
    @SerializedName("watering_period")
    private String watering_period;
    @SerializedName("flowering_season")
    private String flowering_season;
    @SerializedName("description")
    private String description;

    public void setId(int id) {
        this.id = id;
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
