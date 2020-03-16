package io.cityos.cityosair.data.model.reading;

import java.util.Map;

/**
 * Created by Andrej on 07/02/2017.
 */

public class LatestReadings {

    private Map<String, Object> values;

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }
}
