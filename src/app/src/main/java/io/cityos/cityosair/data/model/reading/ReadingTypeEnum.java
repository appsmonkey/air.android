package io.cityos.cityosair.data.model.reading;

import io.cityos.cityosair.R;

public enum ReadingTypeEnum {
  AIR_TEMPERATURE("Temperature", "Temperature"),
  DEVICE_TEMPERATURE("Device Temperature", "Device Temperature"),
  AIR_TEMPERATURE_FEEL("Feels like", "Temperature Feel"),
  AIR_HUMIDITY("Humidity", "Air Humidity"),
  AIR_ALTITUDE("Altitude", "Altitude"),
  UV("UV Light", "uv"),
  LIGHT_INTENSITY("Light", "Light Intensity"),
  AIR_PM1("PM₁", "PM1"),
  AIR_PM2P5("PM₂.₅", "PM2.5"),
  AIR_PM10("PM₁₀", "PM10"),
  NOISE("Noise", "noise"),
  AIR_CO2("CO₂", "CO2"),
  AIR_NO2("NO₂", "NO2"),
  AIR_PRESSURE("Pressure", "Air Pressure"),
  WATER_LEVEL_SWITCH("Water Level", "Water Level"),
  SOIL_MOISTURE("Soil Moisture", "Soil Moisture"),
  BATTERY_PERCENTAGE("Battery Percentage", "Battery Percentage"),
  BATTERY_VOLTAGE("Battery Voltage", "Battery Voltage"),
  UNIDENTIFIED("Unidentified", "");

  private String identifier;
  private String rawEnum;

  ReadingTypeEnum(String identifier, String rawEnum) {
    this.identifier = identifier;
    this.rawEnum = rawEnum;
  }

  public String getIdentifier() {
    return identifier;
  }

  public static String getUnitNotation(ReadingTypeEnum type) {

    switch (type) {
      case AIR_TEMPERATURE:
      case AIR_TEMPERATURE_FEEL:
      case DEVICE_TEMPERATURE:
        return "℃";
      case AIR_HUMIDITY:
      case AIR_CO2:
      case AIR_NO2:
      case SOIL_MOISTURE:
      case BATTERY_PERCENTAGE:
        return "%";
      case AIR_ALTITUDE:
        return "m";
      case UV:
        return "mW/cm³";
      case LIGHT_INTENSITY:
        return "lux";
      case AIR_PM1:
      case AIR_PM10:
      case AIR_PM2P5:
        return "μg/m³";
      case NOISE:
        return "db";
      case AIR_PRESSURE:
        return "hPa";
      case BATTERY_VOLTAGE:
        return "V";
      case UNIDENTIFIED:
        return "";
    }

    return "";
  }

  public static int getImage(ReadingTypeEnum type) {
    switch (type) {
      case AIR_TEMPERATURE:
      case AIR_TEMPERATURE_FEEL:
      case DEVICE_TEMPERATURE:
        return R.drawable.ico_temperature;
      case AIR_HUMIDITY:
        return R.drawable.ico_humidity;
      case AIR_CO2:
        return R.drawable.ico_co;
      case AIR_NO2:
        return R.drawable.ico_gas;
      case AIR_ALTITUDE:
        return R.drawable.ico_altitude;
      case UV:
        return R.drawable.ico_lux;
      case LIGHT_INTENSITY:
        return R.drawable.ico_light;
      case AIR_PM1:
      case AIR_PM10:
      case AIR_PM2P5:
        return R.drawable.ico_pm;
      case NOISE:
        return R.drawable.ico_noise;
      case AIR_PRESSURE:
        return R.drawable.ico_pressure;
      case SOIL_MOISTURE:
        return R.drawable.soilmoisture;
      case WATER_LEVEL_SWITCH:
      case BATTERY_PERCENTAGE:
      case BATTERY_VOLTAGE:
      case UNIDENTIFIED:
        return R.drawable.ic_sensor;
    }

    return 0;
  }

  public static ReadingTypeEnum getReadingType(String rawEnum) {
    for (ReadingTypeEnum type : ReadingTypeEnum.values()) {
      if (type.rawEnum.equals(rawEnum)) {
        return type;
      }
    }

    return UNIDENTIFIED;
  }
}
