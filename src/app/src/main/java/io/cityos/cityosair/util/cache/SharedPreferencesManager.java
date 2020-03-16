package io.cityos.cityosair.util.cache;

public interface SharedPreferencesManager {

  String PACKAGE = "io.cityos.cityosair";
  String LAST_NOTIFICATION = "last_notification";
  String SETTINGS_NOTIFICATION = "settings_notification";
  String REGISTERED = "registered";
  String REGISTERED_TRUE = "registered_true";
  String REGISTERED_FALSE = "registered_false";

  String get(String key, String defaultValue);

  void set(String key, String value);

  void clear();
}
