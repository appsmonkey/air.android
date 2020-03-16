package io.cityos.cityosair.data.model.map;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MapZone extends RealmObject {
  @PrimaryKey @SerializedName("zone_id") private String zoneId;
  @SerializedName("zone_name") private String zoneName;
  @SerializedName("data") private RealmList<ZoneData> zoneData;

  public MapZone() {
  }

  public String getZoneId() {
    return zoneId;
  }

  public String getZoneName() {
    return zoneName;
  }

  public RealmList<ZoneData> getZoneData() {
    return zoneData;
  }
}
