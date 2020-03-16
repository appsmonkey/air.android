package io.cityos.cityosair.util.network;

import com.google.gson.Gson;
import java.lang.reflect.Type;

public interface GsonService {
  Gson getGson();

  byte[] getBytes(Object payload);

  <T> T fromJson(String json, Class<T> clazz);

  <T> T fromJsonWithType(String json, Type type);
}
