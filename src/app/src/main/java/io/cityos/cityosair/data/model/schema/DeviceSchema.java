package io.cityos.cityosair.data.model.schema;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Andrej on 07/02/2017.
 */

public class DeviceSchema extends RealmObject {
    @PrimaryKey
    private int deviceId;
    private int schemaId;
    private RealmList<SchemaReading> schemaReadings;

    public DeviceSchema() {}

    public DeviceSchema(int deviceId, int schemaId, List<SchemaReading> schemaReadings) {
        this.deviceId = deviceId;
        this.schemaId = schemaId;
        this.schemaReadings = new RealmList<>(schemaReadings.toArray(new SchemaReading[schemaReadings.size()]));
    }

    public int getDeviceId() {
        return deviceId;
    }

    public int getSchemaId() {
        return schemaId;
    }

    public List<SchemaReading> getSchemaReadings() {
        return schemaReadings;
    }

    public SchemaReading getReadingForKey(int key) {
        for(SchemaReading reading: schemaReadings) {
            if(reading.getKey() == key) return reading;
        }

        return null;
    }
}
