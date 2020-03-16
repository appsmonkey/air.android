package io.cityos.cityosair.util.cache;

import io.cityos.cityosair.data.messages.requests.AddDevicePayload;
import io.cityos.cityosair.data.model.City;
import io.cityos.cityosair.data.model.Device;
import io.cityos.cityosair.data.model.User;
import io.cityos.cityosair.data.model.map.MapMeta;
import io.cityos.cityosair.data.model.map.MapZone;
import io.cityos.cityosair.data.model.map.ZoneData;
import io.cityos.cityosair.data.model.reading.ReadingCollection;
import io.cityos.cityosair.data.model.schema.DeviceMeasurementCollection;
import io.cityos.cityosair.data.model.schema.DeviceSchema;
import io.cityos.cityosair.data.model.schema.GlobalSchemaReading;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import java.util.List;

/**
 * Created by Andrej on 07/02/2017.
 */

public class CacheUtil<T> {

  private static CacheUtil sharedCache;

  protected CacheUtil() {
  }

  public static CacheUtil getSharedCache() {
    if (sharedCache == null) {
      sharedCache = new CacheUtil();
    }

    return sharedCache;
  }

  public <T extends RealmObject> void save(List<T> list) {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      realm.copyToRealmOrUpdate(list);
      realm.commitTransaction();
    }
  }

  public <T extends RealmObject> void save(T object) {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      realm.copyToRealmOrUpdate(object);
      realm.commitTransaction();
    }
  }

  public void deleteAllDevices() {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      realm.delete(Device.class);
      realm.commitTransaction();
    }
  }

  public void deleteAllZoneData() {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      realm.delete(MapZone.class);
      realm.commitTransaction();
    }
  }

  public void deleteAddDevicePayload() {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      realm.delete(AddDevicePayload.class);
      realm.commitTransaction();
    }
  }

  public void saveAddDevicePayload(AddDevicePayload addDevicePayload) {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      realm.copyToRealmOrUpdate(addDevicePayload);
      realm.commitTransaction();
    }
  }

  public AddDevicePayload getAddDevicePayload() {
    try (Realm realm = Realm.getDefaultInstance()) {
      AddDevicePayload payload = realm.where(AddDevicePayload.class).findFirst();
      return realm.copyFromRealm(payload);
    } catch (Exception e) {
      return null;
    }
  }

  public DeviceMeasurementCollection getDeviceMeasurementCollection(String deviceId) {
    try (Realm realm = Realm.getDefaultInstance()) {
      DeviceMeasurementCollection deviceMeasurementCollection =
              realm.where(DeviceMeasurementCollection.class).equalTo("deviceId", deviceId).findFirst();
      if (deviceMeasurementCollection != null) {
        return realm.copyFromRealm(deviceMeasurementCollection);
      } else {
        return new DeviceMeasurementCollection();
      }
    } catch (Exception e) {
      return null;
    }
  }

  public void saveDevices(List<Device> devices) {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      RealmResults<Device> all = realm.where(Device.class).equalTo("isCityDevice", false).findAll();
      all.deleteAllFromRealm();
      realm.copyToRealmOrUpdate(devices);
      realm.commitTransaction();
    }
  }

  public void updatedPredefined(List<Device> devices) {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      RealmResults<Device> all = realm.where(Device.class).equalTo("isCityDevice", true).findAll();
      all.deleteAllFromRealm();
      realm.copyToRealmOrUpdate(devices);
      realm.commitTransaction();
    }
  }

  public List<Device> getAllDevices() {
    try (Realm realm = Realm.getDefaultInstance()) {
      List<Device> devices = realm.where(Device.class).findAll();
      return realm.copyFromRealm(devices);
    } catch (Exception e) {
      return null;
    }
  }

  public List<Device> getAllMapDevices() {
    try (Realm realm = Realm.getDefaultInstance()) {
      List<Device> devices = realm.where(Device.class).equalTo("isMapDevice", true).findAll();
      return realm.copyFromRealm(devices);
    } catch (Exception e) {
      return null;
    }
  }

  public MapMeta getMapMeta(String deviceId) {
    try (Realm realm = Realm.getDefaultInstance()) {
      MapMeta mapMeta = realm.where(MapMeta.class).equalTo("deviceId", deviceId).findFirst();
      return realm.copyFromRealm(mapMeta);
    } catch (Exception e) {
      return null;
    }
  }

  public List<Device> getMyDevices() {
    try (Realm realm = Realm.getDefaultInstance()) {
      List<Device> devices = realm.where(Device.class).equalTo("mine", true).findAll();
      return realm.copyFromRealm(devices);
    } catch (Exception e) {
      return null;
    }
  }

  public ZoneData getZoneData(String name) {
    try (Realm realm = Realm.getDefaultInstance()) {
      ZoneData zoneData = realm.where(ZoneData.class).equalTo("name", name).findFirst();
      return realm.copyFromRealm(zoneData);
    } catch (Exception e) {
      return null;
    }
  }

  public Device getFirstDevice() {
    try (Realm realm = Realm.getDefaultInstance()) {
      Device device = realm.where(Device.class).equalTo("id", "0RM0X2fnnyDwaJ1XQCpuExixuGW2RmZn").findFirst();
      return realm.copyFromRealm(device);
    } catch (Exception e) {
      return null;
    }
  }

  public Device getDeviceByName(String name) {
    try (Realm realm = Realm.getDefaultInstance()) {
      Device device = realm.where(Device.class).equalTo("name", name).findFirst();
      return realm.copyFromRealm(device);
    } catch (Exception e) {
      return null;
    }
  }

  public Device getDeviceById(String id) {
    try (Realm realm = Realm.getDefaultInstance()) {
      Device device = realm.where(Device.class).equalTo("id", id).findFirst();
      return realm.copyFromRealm(device);
    } catch (Exception e) {
      return null;
    }
  }

  public DeviceSchema getSchema(int deviceId) {
    try (Realm realm = Realm.getDefaultInstance()) {
      String primary = realm.getSchema().get(DeviceSchema.class.getSimpleName()).getPrimaryKey();
      DeviceSchema schema = realm.where(DeviceSchema.class).equalTo(primary, deviceId).findFirst();
      return realm.copyFromRealm(schema);
    } catch (Exception e) {
      return null;
    }
  }

  public ReadingCollection getReadingCollection(int deviceId) {
    try (Realm realm = Realm.getDefaultInstance()) {
      String primary = realm.getSchema().get(ReadingCollection.class.getSimpleName()).getPrimaryKey();
      ReadingCollection collection = realm.where(ReadingCollection.class).equalTo(primary, deviceId).findFirst();
      return realm.copyFromRealm(collection);
    } catch (Exception e) {
      return null;
    }
  }

  public GlobalSchemaReading getGlobalSchemaReading(String id) {
    try (Realm realm = Realm.getDefaultInstance()) {
      return realm.copyFromRealm(realm.where(GlobalSchemaReading.class).equalTo("id", id).findFirst());
    } catch (Exception e) {
      return null;
    }
  }

  public void setCity(City city) {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.beginTransaction();
      realm.copyToRealmOrUpdate(city);

      List<Device> devices = realm.where(Device.class).findAll();
      for (Device device : devices) {
        if (device.isCityDevice()) {
          device.setActive(false);
        } else {
          continue;
        }

        if (device.isCityDevice() && device.getName().equals(city.getDeviceName())) {
          device.setActive(true);
        }
      }
      realm.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public City getCity() {
    try (Realm realm = Realm.getDefaultInstance()) {
      realm.refresh();
      City city = realm.where(City.class).findFirst();
      return realm.copyFromRealm(city);
    } catch (Exception e) {
      return null;
    }
  }

  public User getUser() {
    try (Realm realm = Realm.getDefaultInstance()) {
      User user = realm.where(User.class).findFirst();
      return realm.copyFromRealm(user);
    } catch (Exception e) {

      return null;
    }
  }

  public void logoutUser() {
    try (Realm realm = Realm.getDefaultInstance()) {
      //delete devices expect id 0
      RealmResults<Device> all = realm.where(Device.class).equalTo("isCityDevice", false).findAll();

      //delete reading collection except id 0
      RealmResults<ReadingCollection> collection =
              realm.where(ReadingCollection.class).equalTo("isCityCollection", false).findAll();

      RealmResults<Device> myDevices =
              realm.where(Device.class).equalTo("mine", true).findAll();

      realm.beginTransaction();
      all.deleteAllFromRealm();
      collection.deleteAllFromRealm();

      for (Device myDevice : myDevices) {
        myDevice.setMine(false);
      }

      realm.copyToRealmOrUpdate(myDevices);
      //delete user
      realm.delete(User.class);

      realm.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
