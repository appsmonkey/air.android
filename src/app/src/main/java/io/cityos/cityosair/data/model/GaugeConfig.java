package io.cityos.cityosair.data.model;

import androidx.core.util.Pair;

import io.cityos.cityosair.R;
import io.cityos.cityosair.ui.aqi.AQIEnum;

/**
 * Created by Andrej on 11/03/2017.
 */

public class GaugeConfig {


    private int progressColor, ribbonText, ribbonImage, centerImage;
    private int progressValue, maxValue;

    public GaugeConfig(int progressColor, int ribbonText, int ribbonImage, int centerImage, int progressValue, int maxValue) {
        this.progressColor = progressColor;
        this.ribbonText = ribbonText;
        this.ribbonImage = ribbonImage;
        this.centerImage = centerImage;
        this.progressValue = progressValue;
        this.maxValue = maxValue;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public int getRibbonText() {
        return ribbonText;
    }

    public int getRibbonImage() {
        return ribbonImage;
    }

    public int getCenterImage() {
        return centerImage;
    }

    public int getProgressValue() {
        return progressValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    // gauge config class with all the necessary properties to set the gauge control
    // based on the received data
    static GaugeConfig zero = new GaugeConfig(R.color.aqi_zero_gauge,
        R.string.aqi_zero_ribbon,
        R.drawable.ico_ribbon_great,
        R.drawable.ico_status_great, 0, 60);

    static GaugeConfig great = new GaugeConfig(R.color.aqi_great_gauge,
            R.string.aqi_great_ribbon,
            R.drawable.ico_ribbon_great,
            R.drawable.ico_status_great, 10, 60);

    static GaugeConfig ok = new GaugeConfig(R.color.aqi_ok_gauge,
            R.string.aqi_ok_ribbon,
            R.drawable.ico_ribbon_ok,
            R.drawable.ico_status_ok, 20, 60);

    static GaugeConfig sensitive = new GaugeConfig(R.color.aqi_sensitive_gauge,
            R.string.aqi_sensitive_ribbon,
            R.drawable.ico_ribbon_sensitive,
            R.drawable.ico_status_sensitive, 30, 60);

    static GaugeConfig unhealthy = new GaugeConfig(R.color.aqi_unhealthy_gauge,
            R.string.aqi_unhealthy_ribbon,
            R.drawable.ico_ribbon_unhealthy,
            R.drawable.ico_status_unhealthy, 40, 60);

    static GaugeConfig veryUnhealthy = new GaugeConfig(R.color.aqi_very_unhealthy_gauge,
            R.string.aqi_very_unhealthy_ribbon,
            R.drawable.ico_ribbon_very_unhealthy,
            R.drawable.ico_status_very_unhealthy, 50, 60);

    static GaugeConfig hazardous = new GaugeConfig(R.color.aqi_hazardous_gauge,
            R.string.aqi_hazardous_ribbon,
            R.drawable.ico_ribbon_hazardous,
            R.drawable.ico_status_hazardous, 60, 60);


    public static Pair<GaugeConfig, AQIEnum> getConfigForValue(Double pm10Value, Double pm25Value) {

        AqiIndex pm10Aqi = AqiIndex.getAQIForTypeWithValue(pm10Value, AQIEnum.AIR_PM10);
        AqiIndex pm25Aqi = AqiIndex.getAQIForTypeWithValue(pm25Value, AQIEnum.AIR_PM2P5);

        AQIEnum aqiType;
        AqiIndex aqiToUse;
        GaugeConfig config;

        if (pm10Aqi.getLevel() > pm25Aqi.getLevel()) {
            aqiType = AQIEnum.AIR_PM10;
            aqiToUse = pm10Aqi;
        } else {
            aqiType = AQIEnum.AIR_PM2P5;
            aqiToUse = pm25Aqi;
        }

        if (pm10Aqi == AqiIndex.getAQIForLevel("") && pm25Aqi == AqiIndex.getAQIForLevel("")) {
            config = zero;
            return new Pair<>(config, aqiType);
        }

        switch (aqiToUse.getLevel()) {
            case 0:
                config = zero;
                break;
            case 1:
                config = great;
                break;
            case 2:
                config = ok;
                break;
            case 3:
                config = sensitive;
                break;
            case 4:
                config = unhealthy;
                break;
            case 5:
                config = veryUnhealthy;
                break;
            case 6:
                config = hazardous;
                break;
            default:
                config = great;
        }


        return new Pair<>(config, aqiType);
    }
}
