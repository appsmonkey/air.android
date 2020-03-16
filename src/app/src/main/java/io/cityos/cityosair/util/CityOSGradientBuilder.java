package io.cityos.cityosair.util;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import androidx.core.content.ContextCompat;

import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrej on 12/03/2017.
 */

public class CityOSGradientBuilder {

  public static Context context;

  public static Drawable getDefaultGradient() {

    if (context == null) {
      return null;
    }

    ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
      @Override
      public Shader resize(int width, int height) {
        LinearGradient lg = new LinearGradient(0, 0, 0, height,
            new int[] {
                ContextCompat.getColor(context, R.color.simple_top_gradient),
                ContextCompat.getColor(context, R.color.simple_bottom_gradient)
            },
            new float[] { 0, 1 },
            Shader.TileMode.REPEAT);
        return lg;
      }
    };

    PaintDrawable p = new PaintDrawable();
    p.setShape(new RectShape());
    p.setShaderFactory(sf);
    p.setAlpha(168);

    return p;
  }

  public static Drawable getGreatGradient() {

    if (context == null) {
      return null;
    }

    ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
      @Override
      public Shader resize(int width, int height) {
        LinearGradient lg = new LinearGradient(0, 0, 0, height,
            new int[] {
                ContextCompat.getColor(context, R.color.great_gradient),
                ContextCompat.getColor(context, R.color.great_gradient)
            },
            new float[] { 0, 1 },
            Shader.TileMode.REPEAT);
        return lg;
      }
    };

    PaintDrawable p = new PaintDrawable();
    p.setShape(new RectShape());
    p.setShaderFactory(sf);
    p.setAlpha(168);

    return p;
  }

  public static Drawable getPMGradient(int numberGradientsToLeave, int xMax, ReadingTypeEnum type) {

    if (context == null) {
      return null;
    }

    int[] startColors = new int[] {
        ContextCompat.getColor(context, R.color.hazardous_gradient),
        ContextCompat.getColor(context, R.color.very_unhealthy_gradient),
        ContextCompat.getColor(context, R.color.unhealthy_gradient),
        ContextCompat.getColor(context, R.color.sensitive_gradient),
        ContextCompat.getColor(context, R.color.ok_gradient),
        ContextCompat.getColor(context, R.color.great_gradient)
    };

    if (numberGradientsToLeave == 7) {
      numberGradientsToLeave = 6;
    }

    if (numberGradientsToLeave < 2) {
      numberGradientsToLeave = 2;
    }

    if (type == ReadingTypeEnum.AIR_PM2P5) {
      if (xMax < 251 && numberGradientsToLeave == 6) {
        numberGradientsToLeave = 5;
      }
    } else if (xMax < 425 && numberGradientsToLeave == 6) {
      numberGradientsToLeave = 5;
    }

    int[] colors = Arrays.copyOfRange(startColors, 6 - numberGradientsToLeave, startColors.length);

    float incremental = 1.0f / (float) (numberGradientsToLeave - 1);

    List<Float> startLocations = new ArrayList<>();
    startLocations.add(0f);

    for (int i = 1; i < numberGradientsToLeave; i++) {
      startLocations.add(incremental * (float) i);
    }

    float[] locations = new float[startLocations.size()];

    int i = 0;

    for (Float f : startLocations) {
      locations[i++] = (f != null ? f : Float.NaN);
    }

    ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
      @Override
      public Shader resize(int width, int height) {
        LinearGradient lg = new LinearGradient(0, 0, 0, height,
            colors,
            locations,
            Shader.TileMode.REPEAT);
        return lg;
      }
    };

    PaintDrawable p = new PaintDrawable();
    p.setShape(new RectShape());
    p.setShaderFactory(sf);
    p.setAlpha(168);

    return p;
  }
}
