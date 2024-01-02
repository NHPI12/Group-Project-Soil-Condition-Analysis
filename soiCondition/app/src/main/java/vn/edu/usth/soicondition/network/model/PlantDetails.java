package vn.edu.usth.soicondition.network.model;

public class PlantDetails {
    private String commonName;
    private String thumbnailUrl;

    public PlantDetails(String commonName, String thumbnailUrl) {
        this.commonName = commonName;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}