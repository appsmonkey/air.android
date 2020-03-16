package io.cityos.cityosair.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Andrej on 25/05/2017.
 */

public class City extends RealmObject {

    @PrimaryKey
    private int id = 1;

    private int cityId = 0;
    private String name;
    private String deviceName;
    private Double lat;
    private Double lng;

    public City() {}

    public City(int cityId, String name, String deviceName, Double lat, Double lng) {
        this.cityId = cityId;
        this.name = name;
        this.deviceName = deviceName;
        this.lat = lat;
        this.lng = lng;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public static City sarajevoCity = new City(1, "Sarajevo", "Sarajevo Air", 43.84195592377653, 18.36536407470703);
    public static City belgradeCity = new City(3, "Belgrade", "Belgrade Air", 44.786568, 20.448922);
}
