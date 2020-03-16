package io.cityos.cityosair.data.model.reading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cityos.cityosair.data.model.schema.DeviceSchema;
import io.cityos.cityosair.data.model.schema.SchemaReading;
import io.cityos.cityosair.util.app.CityOSDateUtils;

/**
 * Created by Andrej on 07/02/2017.
 */

public class ReadingParser {
    private List<Reading> readings = new ArrayList<>();
    private String timestamp;

    public ReadingParser(Map<String,Object> latest, DeviceSchema schema) {
        Map<String, Object> values = latest;

        if (values == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : values.entrySet())  {
            if(entry.getKey() != null && entry.getValue() != null) {

                if(entry.getKey().equals("timestamp")) {
                    this.timestamp = CityOSDateUtils.getRelativeDateTime((String) entry.getValue());
                    continue;
                }

                int key = Integer.parseInt(entry.getKey());
                double value = (Double) entry.getValue();

                SchemaReading schemaReading = schema.getReadingForKey(key);
                if(schemaReading != null) {
                    ReadingTypeEnum current = schemaReading.getReadingTypeEnum();
                    if (current != ReadingTypeEnum.UNIDENTIFIED && !schemaReading.getAdditional().equals("range")) {
                        Reading reading = new Reading(schemaReading.getReadingTypeRaw(), value, key);
                        this.readings.add(reading);
                    }
                }
            }
        }
    }

    public List<Reading> getReadings() {
        return this.readings;
    }

    public String getTimestamp() { return  this.timestamp; }

}
