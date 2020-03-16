package io.cityos.cityosair.data.model;

import io.cityos.cityosair.R;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import java.util.Arrays;
import java.util.List;

public class AqiIndex {
  private int titleResource, textResource, valueResource, imageResource, mapResource,
      backgroundColorResource, fillColor, strokeColor, textColorResource, level,
      selectedMapResource, myMapResource, mySelectedMapResource;

  public AqiIndex(int title, int text, int value, int imageResource, int mapResource,
      int backgroundColorResource, int fillColor, int strokeColor, int textColorResource,
      int level, int selectedMapResource, int myMapResource, int mySelectedMapResource) {
    this.titleResource = title;
    this.textResource = text;
    this.valueResource = value;
    this.imageResource = imageResource;
    this.mapResource = mapResource;
    this.backgroundColorResource = backgroundColorResource;
    this.fillColor = fillColor;
    this.strokeColor = strokeColor;
    this.textColorResource = textColorResource;
    this.level = level;
    this.selectedMapResource = selectedMapResource;
    this.myMapResource = myMapResource;
    this.mySelectedMapResource = mySelectedMapResource;
  }

  public int getMySelectedMapResource() {
    return mySelectedMapResource;
  }

  public int getMyMapResource() {
    return myMapResource;
  }

  public int getSelectedMapResource() {
    return selectedMapResource;
  }

  public int getTitle() {
    return titleResource;
  }

  public int getText() {
    return textResource;
  }

  public int getValue() {
    return valueResource;
  }

  public int getImageResource() {
    return imageResource;
  }

  public int getMapResource() {
    return mapResource;
  }

  public int getBackgroundColorResource() {
    return backgroundColorResource;
  }

  public int getFillColor() {
    return fillColor;
  }

  public int getStrokeColor() {
    return strokeColor;
  }

  public int getTextColorResource() {
    return textColorResource;
  }

  public int getLevel() {
    return level;
  }

  public void setValue(int valueResource) {
    this.valueResource = valueResource;
  }

  //All indexes
  private static AqiIndex zero = new AqiIndex(R.string.aqi_zero_title,
      R.string.aqi_zero,
      R.string.aqi_zero,
      R.drawable.ico_aqi_great,
      R.drawable.box_gray,
      R.color.aqi_great_background,
      R.color.map_fill_zero,
      R.color.map_stroke_zero,
      R.color.aqi_zero_text, 0,
      R.drawable.box_gray_selected,
      R.drawable.box_gray_my,
      R.drawable.box_gray_my_selected);

  private static AqiIndex great = new AqiIndex(R.string.aqi_great_title,
      R.string.aqi_great_text,
      R.string.aqi_10_great_value,
      R.drawable.ico_aqi_great,
      R.drawable.box_blue,
      R.color.aqi_great_background,
      R.color.map_fill_great,
      R.color.map_stroke_great,
      R.color.aqi_great_text, 1,
      R.drawable.box_blue_selected,
      R.drawable.box_blue_my,
      R.drawable.box_blue_my_selected);

  private static AqiIndex ok = new AqiIndex(R.string.aqi_ok_title,
      R.string.aqi_ok_text,
      R.string.aqi_10_ok_value,
      R.drawable.ico_aqi_ok,
      R.drawable.box_green,
      R.color.aqi_ok_background,
      R.color.map_fill_ok,
      R.color.map_stroke_ok,
      R.color.aqi_ok_text, 2,
      R.drawable.box_green_selected,
      R.drawable.box_green_my,
      R.drawable.box_green_my_selected);

  private static AqiIndex sensitive = new AqiIndex(R.string.aqi_sensitive_title,
      R.string.aqi_sensitive_text,
      R.string.aqi_10_sensitive_value,
      R.drawable.ico_aqi_sensitive,
      R.drawable.box_yellow,
      R.color.aqi_sensitive_background,
      R.color.map_fill_sensitive,
      R.color.map_stroke_sensitive,
      R.color.aqi_sensitive_text, 3,
      R.drawable.box_yellow_selected,
      R.drawable.box_yellow_my,
      R.drawable.box_yellow_my_selected);

  private static AqiIndex unhealthy = new AqiIndex(R.string.aqi_unhealthy_title,
      R.string.aqi_unhealthy_text,
      R.string.aqi_10_unhealthy_value,
      R.drawable.ico_aqi_unhealthy,
      R.drawable.box_orange,
      R.color.aqi_unhealthy_background,
      R.color.map_fill_unhealthy,
      R.color.map_stroke_unhealthy,
      R.color.aqi_unhealthy_text, 4,
      R.drawable.box_orange_selected,
      R.drawable.box_orange_my,
      R.drawable.box_orange_my_selected);

  private static AqiIndex veryUnhealthy = new AqiIndex(R.string.aqi_very_unhealthy_title,
      R.string.aqi_very_unhealthy_text,
      R.string.aqi_10_very_unhealthy_value,
      R.drawable.ico_aqi_very_unhealthy,
      R.drawable.box_purple,
      R.color.aqi_very_unhealthy_background,
      R.color.map_fill_very_unhealthy,
      R.color.map_stroke_very_unhealthy,
      R.color.aqi_very_unhealthy_text, 5,
      R.drawable.box_purple_selected,
      R.drawable.box_purple_my,
      R.drawable.box_purple_my_selected);

  private static AqiIndex hazardous = new AqiIndex(R.string.aqi_hazardous_title,
      R.string.aqi_hazardous_text,
      R.string.aqi_10_hazardous_value,
      R.drawable.ico_aqi_hazardous,
      R.drawable.box_red,
      R.color.aqi_hazardous_background,
      R.color.map_fill_hazardous,
      R.color.map_stroke_hazardous,
      R.color.aqi_hazardous_text, 6,
      R.drawable.box_red_selected,
      R.drawable.box_red_my,
      R.drawable.box_red_my_selected);

  private static void setValuesForType(AQIEnum aqiType) {

    if (aqiType == AQIEnum.AIR_PM10) {

      great.setValue(R.string.aqi_10_great_value);
      ok.setValue(R.string.aqi_10_ok_value);
      sensitive.setValue(R.string.aqi_10_sensitive_value);
      unhealthy.setValue(R.string.aqi_10_unhealthy_value);
      veryUnhealthy.setValue(R.string.aqi_10_very_unhealthy_value);
      hazardous.setValue(R.string.aqi_10_hazardous_value);
    } else {

      great.setValue(R.string.aqi_2_5_great_value);
      ok.setValue(R.string.aqi_2_5_ok_value);
      sensitive.setValue(R.string.aqi_2_5_sensitive_value);
      unhealthy.setValue(R.string.aqi_2_5_unhealthy_value);
      veryUnhealthy.setValue(R.string.aqi_2_5_very_unhealthy_value);
      hazardous.setValue(R.string.aqi_2_5_hazardous_value);
    }
  }

  public static List<AqiIndex> getAllAQIsForType(AQIEnum aqiType) {

    setValuesForType(aqiType);

    return Arrays.asList(great, ok, sensitive, unhealthy, veryUnhealthy, hazardous);
  }

  public static AqiIndex getAQIForLevel(String level) {
    switch (level.toLowerCase()) {
      case "great":
        return great;
      case "ok":
          return ok;
      case "sensitive beware":
        return sensitive;
      case "unhealthy":
        return unhealthy;
      case "very unhealthy":
        return veryUnhealthy;
      case "hazardous":
        return hazardous;
      default:
        return zero;
    }
  }

  public static AqiIndex getAQIForTypeWithValue(Double value, AQIEnum aqiType) {

    setValuesForType(aqiType);

    if (value == null) {
      return zero;
    }

    if (aqiType == AQIEnum.AIR_PM10) {

      if (value == -1) {
        return zero;
      } else if (isBetween(value, 0, 54)) {
        return great;
      } else if (isBetween(value, 55, 154)) {
        return ok;
      } else if (isBetween(value, 155, 254)) {
        return sensitive;
      } else if (isBetween(value, 255, 354)) {
        return unhealthy;
      } else if (isBetween(value, 355, 424)) {
        return veryUnhealthy;
      } else {
        if (value > 424) {
          return hazardous;
        } else {
          return great;
        }
      }
    } else {

      if (value == -1) {
        return zero;
      } else if (isBetween(value, 0, 12)) {
        return great;
      } else if (isBetween(value, 12.1, 35.4)) {
        return ok;
      } else if (isBetween(value, 35.5, 55.4)) {
        return sensitive;
      } else if (isBetween(value, 55.5, 150.4)) {
        return unhealthy;
      } else if (isBetween(value, 150.5, 250.4)) {
        return veryUnhealthy;
      } else if (isBetween(value, 250.5, 500.4)) {
        return hazardous;
      } else {
        if (value > 500.4) {
          return hazardous;
        } else {
          return great;
        }
      }
    }
  }

  //HELPER METHOD
  private static boolean isBetween(double x, double lower, double upper) {
    return lower <= x && x <= upper;
  }
}
