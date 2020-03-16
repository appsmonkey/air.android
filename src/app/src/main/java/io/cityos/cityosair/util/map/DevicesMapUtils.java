package io.cityos.cityosair.util.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import io.cityos.cityosair.app.Constants;
import io.cityos.cityosair.data.model.AqiIndex;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.ui.aqi.AQIEnum;

public class DevicesMapUtils {
  public static String getSensor(Device device) {
    double pm10Value = device.getMapMeta().get(Constants.AIR_PM10).getValue();
    double pm25Value = device.getMapMeta().get(Constants.AIR_PM2P5).getValue();
    AqiIndex aqiPm10Value = AqiIndex.getAQIForTypeWithValue(pm10Value, AQIEnum.AIR_PM10);
    AqiIndex aqiPm25Value = AqiIndex.getAQIForTypeWithValue(pm25Value, AQIEnum.AIR_PM2P5);
    String sensor = Constants.AIR_PM10;
    if (aqiPm25Value.getLevel() > aqiPm10Value.getLevel()) {
      sensor = Constants.AIR_PM2P5;
    }
    return sensor;
  }

  public static Bitmap getBitmapIcon(Context context, Device device) {
    String sensor = getSensor(device);

    AqiIndex aqiIndex = device.isMine() ? device.getAQI(sensor) : device.getAQI(sensor);
    if (!device.isActive()) {
      aqiIndex = AqiIndex.getAQIForLevel("");
    }
    int resource = device.isMine() ? aqiIndex.getMyMapResource() : aqiIndex.getMapResource();
    Bitmap icon = BitmapFactory.decodeResource(context.getResources(), resource);

    return icon;
  }

  public static Bitmap getSelectedBitmapIcon(Context context, Device device) {
    String sensor = getSensor(device);

    AqiIndex aqiIndex = device.isMine() ? device.getAQI(sensor) : device.getAQI(sensor);
    if (!device.isActive()) {
      aqiIndex = AqiIndex.getAQIForLevel("");
    }
    int resource = device.isMine() ? aqiIndex.getMySelectedMapResource() : aqiIndex.getSelectedMapResource();
    Bitmap icon = BitmapFactory.decodeResource(context.getResources(), resource);

    return icon;
  }


}
