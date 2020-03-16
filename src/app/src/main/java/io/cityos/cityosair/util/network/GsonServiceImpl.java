package io.cityos.cityosair.util.network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.realm.RealmObject;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class GsonServiceImpl implements GsonService {

  private Gson gson;

  @Override public Gson getGson() {
    return gson;
  }

  @Inject public GsonServiceImpl() {
    this.gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
      @Override public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaringClass().equals(RealmObject.class);
      }

      @Override public boolean shouldSkipClass(Class<?> clazz) {
        return false;
      }
    }).serializeNulls().create();
  }

  @Override public byte[] getBytes(Object payload) {
    return gson.toJson(payload).getBytes();
  }

  public <T> T fromJson(String json, Class<T> clazz) {
    return gson.fromJson(json, clazz);
  }

  @Override public <T> T fromJsonWithType(String json, Type type) {
    return gson.fromJson(json, type);
  }
}
