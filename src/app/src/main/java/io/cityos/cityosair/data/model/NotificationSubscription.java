package io.cityos.cityosair.data.model;

public class NotificationSubscription {
  String application;
  TopicsEntity rel;

  public TopicsEntity getRel() {
    return rel;
  }

  public String getApplication() {
    return application;
  }
}
