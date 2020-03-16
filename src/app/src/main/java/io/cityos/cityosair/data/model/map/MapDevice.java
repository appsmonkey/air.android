package io.cityos.cityosair.data.model.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.cityos.cityosair.data.model.AqiIndex;
import io.cityos.cityosair.ui.aqi.AQIEnum;

/**
 * Created by Andrej on 23/04/2017.
 */

public class MapDevice {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("values")
    @Expose
    private Values values;

    @SerializedName("location")
    @Expose
    private Location location;

    public String getName() {
        return name;
    }

    public Boolean has25() {
        return values.getPm_25() != null;
    }

    public Boolean has10() {
        return values.getPm_10() != null;
    }

    public Double getPm_25() {
//        if(values.getPm_25() == null)
//            return 0.0;
        return values.getPm_25();
    }

    public Double getPm_10() {
//        if(values.getPm_10() == null)
//            return 0.0;
        return values.getPm_10();
    }

    public Double getTemp() {
        if(values.getTemperature() == null)
            return 0.0;
        return values.getTemperature();
    }

    public AqiIndex getAQI() {

        AqiIndex pm25Aqi = AqiIndex.getAQIForTypeWithValue(getPm_25(), AQIEnum.AIR_PM2P5);
        AqiIndex pm10Aqi = AqiIndex.getAQIForTypeWithValue(getPm_10(), AQIEnum.AIR_PM10);

        return  pm25Aqi.getLevel() > pm10Aqi.getLevel() ? pm25Aqi : pm10Aqi;
    }

    //Boilerplate

    public void setName(String name) {
        this.name = name;
    }

    public Values getValues() {
        return values;
    }

    public void setValues(Values values) {
        this.values = values;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public class Location {

        @SerializedName("latitude")
        @Expose
        private Double latitude;

        @SerializedName("longitude")
        @Expose
        private Double longitude;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

    }

    public class Values {

        @SerializedName("1")
        @Expose
        private Double temperature;

        @SerializedName("5")
        @Expose
        private Double pm_25;

        @SerializedName("6")
        @Expose
        private Double pm_10;

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Double getPm_25() {
            return pm_25;
        }

        public void setPm_25(Double pm_25) {
            this.pm_25 = pm_25;
        }

        public Double getPm_10() {
            return pm_10;
        }

        public void setPm_10(Double pm_10) {
            this.pm_10 = pm_10;
        }
    }

}