package vn.edu.usth.soicondition.model;

public class PlantListItem {
    private String common_name;
    private int thumbnail, watering,sunlight,cycle;



    public PlantListItem(String common_name, int imageTile, int watering, int sunlight, int cycle ){
        this.common_name = common_name;
        this.thumbnail = imageTile;
        this.watering = watering;
        this.sunlight = sunlight;
        this.cycle = cycle;
    }
    public PlantListItem(){
    }
    public void setCommon_name(String common_name){
        this.common_name = common_name;
    }
    public  String getCommon_name(){
        return common_name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getWatering() {
        return watering;
    }

    public void setWatering(int watering_icon) {
        this.watering = watering_icon;
    }
    public int getSunlight() {
        return sunlight;
    }

    public void setSunlight(int sunlight) {
        this.sunlight = sunlight;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }
}
