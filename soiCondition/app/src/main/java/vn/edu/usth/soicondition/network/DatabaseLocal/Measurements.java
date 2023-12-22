package vn.edu.usth.soicondition.network.DatabaseLocal;

public class Measurements {
    private String timestamps;
    private float temperature;
    private float humidity;
    private float soil_moisture;

    public float getTemperature() {
        return temperature;
    }

    public float getSoil_moisture() {
        return soil_moisture;
    }

    public float getHumidity() {
        return humidity;
    }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setSoil_moisture(float soil_moisture) {
        this.soil_moisture = soil_moisture;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }
}
