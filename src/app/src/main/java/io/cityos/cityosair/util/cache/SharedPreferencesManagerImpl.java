package io.cityos.cityosair.util.cache;

import android.content.Context;
import android.content.SharedPreferences;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesManagerImpl implements SharedPreferencesManager {

  private final SharedPreferences prefs;
  Context context;

  @Inject
  public SharedPreferencesManagerImpl(Context context) {
    this.context = context;
    this.prefs = context.getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);
  }

  @Override public String get(String key, String defaultValue) {
    return prefs.getString(key, defaultValue);
  }

  @Override public void set(String key, String value) {
    prefs.edit().putString(key, value).apply();
  }

  @Override public void clear() {
    prefs.edit().clear().apply();
  }
}
