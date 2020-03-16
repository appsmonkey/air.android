package io.cityos.cityosair.data.model.map;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import io.cityos.cityosair.R;

/**
 * Created by Andrej on 27/04/2017.
 */

public class MapPlace {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("polygon")
    @Expose
    private List<PolygonPoint> polygon = null;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<PolygonPoint> getPolygon() {
        return polygon;
    }
    public void setPolygon(List<PolygonPoint> polygon) {
        this.polygon = polygon;
    }

    public static List<MapPlace> getPlaces(Resources resources, int cityId) {
        Gson gson = new Gson();
        BufferedReader bufferedReader = null;
        try {
            InputStream places = cityId == 1
                    ? resources.openRawResource(R.raw.places_sarajevo)
                    : resources.openRawResource(R.raw.places_belgrade);
            bufferedReader = new BufferedReader(new InputStreamReader(places, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        Type type = new TypeToken<List<MapPlace>>(){}.getType();
        List<MapPlace> places = gson.fromJson(bufferedReader, type);

        return places;
    }

    public class PolygonPoint {
        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        public Double getLat() {
            return lat;
        }
        public void setLat(Double lat) {
            this.lat = lat;
        }
        public Double getLng() {
            return lng;
        }
        public void setLng(Double lng) {
            this.lng = lng;
        }

    }
}
