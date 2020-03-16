package io.cityos.cityosair.util;

import io.cityos.cityosair.data.model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrej on 08/02/2017.
 */

public class PredefinedDevices {

  private List<Device> devices = new ArrayList<>();

  public PredefinedDevices(Boolean debug) {

    //City currentCity = CacheUtil.getSharedCache().getCity();
    //
    //boolean isSarajevo = currentCity == null ? true : currentCity.getCityId() == 1;
    //
    //Device sarajevoDevice = new Device();
    //sarajevoDevice.setDeviceId(1);
    //sarajevoDevice.setName("Sarajevo Air");
    //sarajevoDevice.setSchemaId(1);
    //sarajevoDevice.setCityDevice(true);
    //sarajevoDevice.setActive(isSarajevo);
    //
    //Device belgradeDevice = new Device();
    //belgradeDevice.setDeviceId(3);
    //belgradeDevice.setName("Belgrade Air");
    //belgradeDevice.setSchemaId(2);
    //belgradeDevice.setCityDevice(true);
    //belgradeDevice.setActive(!isSarajevo);
    //
    //List<SchemaReading> schemaReadings = new ArrayList<>();
    //
    //SchemaReading temp = new SchemaReading(1, "air", "temperature", "");
    //SchemaReading hum = new SchemaReading(2, "air", "humidity", "");
    //SchemaReading temp_feel = new SchemaReading(3, "air", "temperature_feel", "");
    //SchemaReading pm1 = new SchemaReading(4, "air", "pm1", "");
    //SchemaReading pm25 = new SchemaReading(5, "air", "pm2.5", "");
    //SchemaReading pm10 = new SchemaReading(6, "air", "pm10", "");
    //
    //schemaReadings.addAll(Arrays.asList(temp, hum, temp_feel, pm1, pm25, pm10));
    //
    //DeviceSchema sarajevoSchema = new DeviceSchema(1, 1, schemaReadings);
    //DeviceSchema belgradeSchema = new DeviceSchema(3, 2, schemaReadings);

    //CacheUtil.getSharedCache().save(sarajevoSchema);
    //CacheUtil.getSharedCache().save(belgradeSchema);

    //devices.add(sarajevoDevice);
    //devices.add(belgradeDevice);

    //        if(debug) {
    //            Device selmaDevice = new Device();
    //            selmaDevice.setActive(true);
    //            selmaDevice.setDeviceId(193145);
    //            selmaDevice.setName("Selma's Air");
    //            selmaDevice.setSchemaId(31);
    //
    //            devices.add(selmaDevice);
    //        }
  }

  public List<Device> getPredefinedDevices() {
    return this.devices;
  }
}
