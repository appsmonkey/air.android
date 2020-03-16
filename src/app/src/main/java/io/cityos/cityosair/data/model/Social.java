package io.cityos.cityosair.data.model;

public class Social {

  public enum TYPE {
    G,
    FB
  }

  private String id;
  private String token;
  private TYPE type;

  public Social(String id, String token, TYPE type) {
    this.id = id;
    this.token = token;
    this.type = type;
  }
}
