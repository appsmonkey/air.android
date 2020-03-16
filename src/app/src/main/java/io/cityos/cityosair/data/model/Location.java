package io.cityos.cityosair.data.model;

import io.realm.RealmObject;

/**
 * Created by Andrej on 07/02/2017.
 */

public class Location extends RealmObject {

    private Double latitude;
    private Double longitude;

    public Location() {}

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
