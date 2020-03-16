package io.cityos.cityosair.data.model.reading;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Andrej on 07/02/2017.
 */

public class Reading extends RealmObject implements Serializable {

    private String readingTypeRaw;
    private Double value;
    private int sensorId;
    private ReadingType readingType;

    public Reading() {}

    public Reading(String readingType, Double value, int sensorId) {
        this.readingTypeRaw = readingType;
        this.value = value;
        this.sensorId = sensorId;
    }

    public Double getValue() {
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round(value * scale) / scale;
    }

    public int getSensorId() {
        return sensorId;
    }

    public ReadingType getReadingType() {

        if(readingType == null) {
            readingType = new ReadingType(readingTypeRaw);
        }

        return readingType;
    }
}
