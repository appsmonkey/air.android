package io.cityos.cityosair.data.model.schema;

import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;
import io.realm.RealmObject;

/**
 * Created by Andrej on 07/02/2017.
 */

public class SchemaReading extends RealmObject {

    private int key;
    private String whereType;
    private String additional;
    private String readingTypeRaw;

    public SchemaReading() {}

    public SchemaReading(int key, String whereType, String readingType, String additional) {
        this.key = key;
        this.whereType = whereType;
        this.readingTypeRaw = readingType;
        this.additional = additional;
    }

    public int getKey() {
        return key;
    }

    public String getWhereType() {
        return whereType;
    }

    public String getAdditional() {
        return additional;
    }

    public String getReadingTypeRaw() {
        return readingTypeRaw;
    }

    public ReadingTypeEnum getReadingTypeEnum() {
        return ReadingTypeEnum.getReadingType(readingTypeRaw);
    }
}
