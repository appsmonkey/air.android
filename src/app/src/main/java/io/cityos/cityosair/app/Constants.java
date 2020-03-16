package io.cityos.cityosair.app;

public class Constants {
    // app
    public static final String FIRST_RUN_KEY = "first_run";
    public static final String BASE_URL = "http://ctos.io";
    public static final String FIR_BASE_URL = "https://iid.googleapis.com";
    public static final String FIR_AUTH_KEY =  "FIREBASE_AUTH_KEY";

    // cities
    public static final String SARAJEVO_CITY_AIR = "Sarajevo Air";
    public static final String SARAJEVO_CITY = "Sarajevo";

    // intents
    public static final Integer SETTINGS_REQUEST_CODE = 103;
    public static final int GOOGLE_RESULT_CODE = 101;

    public static final String ADD_DEVICE_PAYLOAD = "add_device_payload";
    public static final String DEVICE_TOKEN = "device_token";

    // measurements
    public static final String AIR_PM10 = "AIR_PM10";
    public static final String AIR_PM2P5 = "AIR_PM2P5";
    public static final String AIR_TEMPERATURE = "AIR_TEMPERATURE";

    public static final String MAP_READING_SENSOR = "AIR_PM10,AIR_PM2P5,AIR_TEMPERATURE,AIR_AQI_RANGE";
    public static final String MAP_READING_DATA = "AIR_PM10,AIR_PM2P5,AIR_TEMPERATURE,AIR_AQI_RANGE";

    // deep link
    public static final String VERIY_EMAIL_REGISTRATION_TYPE = "verify";
    public static final String VERIY_EMAIL_PASSWORD_TYPE = "pwreset";

    public static final String API_BASE_DEV_URL = "https://apigway.cityos.io/";
    public static final String API_BASE_PROD_URL = "https://api.cityos.io";
}
