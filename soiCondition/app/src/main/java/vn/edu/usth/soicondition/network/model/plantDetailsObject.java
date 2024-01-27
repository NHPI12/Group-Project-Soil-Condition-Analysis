package vn.edu.usth.soicondition.network.model;

public class plantDetailsObject {
    int id;
    String care_level;
    String watering_period;
    public plantDetailsObject(int id, String care_level, String watering_period){
        this.id = id;
        this.care_level = care_level;
        this.watering_period = watering_period;
    }

    public String getCare_level(){
        return care_level;
    }
    public String getWatering_period(){
        return watering_period;
    }
    public int getId(){
        return id;
    }
}
