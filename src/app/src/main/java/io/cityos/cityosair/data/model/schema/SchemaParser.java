package io.cityos.cityosair.data.model.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Andrej on 07/02/2017.
 */

public class SchemaParser {

    private List<SchemaReading> readings = new ArrayList<>();

    public SchemaParser(Schema schema) {
        Map<String, String> values = schema.getLayout().getSense();

        if(values == null) {
            return;
        }

        for (Map.Entry<String, String> entry : values.entrySet())
        {
            int key = Integer.parseInt(entry.getKey());

            String[] schemaValues = entry.getValue().split(" ");

            if (schemaValues.length == 1) {
                continue;
            }

            String whereType = schemaValues[0];
            String whatType = schemaValues[1];
            String additional = "";

            if(whatType.equals("pm")) {
                whatType += schemaValues[2];

                if(schemaValues.length == 4) {
                    additional = schemaValues[3];
                }
            } else if(schemaValues.length == 3) {
                additional = schemaValues[2];
            }

            SchemaReading reading = new SchemaReading(key, whereType, whatType, additional);
            
            this.readings.add(reading);

        }
    }

    public List<SchemaReading> getReadingsSchema() {
        return this.readings;
    }
}
