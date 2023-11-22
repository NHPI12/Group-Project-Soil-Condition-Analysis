package vn.edu.usth.soicondition.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlantResponse {
    public static final String PLANT_DATA = "data";
    @SerializedName(PLANT_DATA)
    private List<PlantData> plantDataList;

    public List<PlantData> getPlantDataList() {
        return plantDataList;
    }

    public void setPlantDataList(List<PlantData> plantDataList) {
        this.plantDataList = plantDataList;
    }
}
