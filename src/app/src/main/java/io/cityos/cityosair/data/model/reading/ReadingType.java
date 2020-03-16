package io.cityos.cityosair.data.model.reading;

import io.realm.RealmObject;
import java.io.Serializable;

public class ReadingType extends RealmObject implements Serializable {

  private String typeRaw;

  public ReadingType() {
  }

  public ReadingType(String typeRaw) {
    this.typeRaw = typeRaw;
  }

  public String getIdentifier() {
    return getEnumValue().getIdentifier();
  }

  public String getUnitNotation() {
    return ReadingTypeEnum.getUnitNotation(getEnumValue());
  }

  public int getDrawable() {
    ReadingTypeEnum enumValue = getEnumValue();
    return ReadingTypeEnum.getImage(enumValue);
  }

  public ReadingTypeEnum getEnumValue() {
    String typeRaw = this.typeRaw;
    return ReadingTypeEnum.getReadingType(typeRaw);
  }

  public String getEnumIdentifier() {
    return ReadingTypeEnum.getReadingType(typeRaw).getIdentifier();
  }
}