package io.cityos.cityosair.data.model.reading;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Andrej on 07/02/2017.
 */

public class ReadingCollection extends RealmObject {

    @PrimaryKey
    private int id;
    private String lastUpdated;
    private RealmList<Reading> readings;
    private boolean isCityCollection = false;

    public ReadingCollection() {}

    public ReadingCollection(String lastUpdated, List<Reading> readings, int deviceId, boolean isCityCollection) {
        this.id = deviceId;
        this.lastUpdated = lastUpdated;
        this.readings = new RealmList<>(readings.toArray(new Reading[readings.size()]));
        this.isCityCollection = isCityCollection;
    }

    public int getId() {
        return id;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public RealmList<Reading> getReadings() {
        return readings;
    }

    public boolean isCityCollection() {
        return isCityCollection;
    }

    public void setCityCollection(boolean cityCollection) {
        isCityCollection = cityCollection;
    }

    public double getValueForReadingType(ReadingTypeEnum readingType) {

        for(Reading reading: readings) {
            if(reading.getReadingType().getEnumValue() == readingType) {
                return reading.getValue();
            }
        }

        return 0;
    }
}
