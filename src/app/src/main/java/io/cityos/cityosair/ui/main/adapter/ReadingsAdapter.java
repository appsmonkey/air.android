package io.cityos.cityosair.ui.main.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;
import io.cityos.cityosair.data.model.schema.DeviceMeasurement;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import io.cityos.cityosair.ui.graph.GraphActivity;
import io.cityos.cityosair.ui.main.adapter.viewholder.ExtendedReadingHolder;
import io.cityos.cityosair.ui.main.adapter.viewholder.ReadingHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadingsAdapter extends RecyclerView.Adapter {

  public interface RowType {
    int READING_ROW_TYPE = 0;
    int EXTENDED_READING_ROW_TYPE = 1;
  }

  private List<DeviceMeasurement> collection;
  private Context context;
  private String deviceId;

  public ReadingsAdapter() {
    this.collection = new ArrayList<>();
  }

  public void updateDataSet(List<DeviceMeasurement> data, AQIEnum aqiType, String deviceId) {
    this.deviceId = deviceId;

    Collections.sort(data, (o1, o2) -> {
      if (o1.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM10) {
        if (o2.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM2P5) {
          if (aqiType == AQIEnum.AIR_PM10) {
            return -1;
          } else {
            return 1;
          }
        } else {
          return -1;
        }
      } else if (o1.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM2P5) {
        if (o2.getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM10) {
          if (aqiType == AQIEnum.AIR_PM2P5) {
            return -1;
          } else {
            return 1;
          }
        } else {
          return -1;
        }
      } else {
        return 0;
      }
    });

    List<DeviceMeasurement> measurements = new ArrayList<>();
    for (DeviceMeasurement measurement : data) {
      if (measurement.getName() != null && !measurement.getName().toLowerCase().contains("range")) {
        measurements.add(measurement);
      }
    }
    this.collection = measurements;
    notifyDataSetChanged();
  }

  @Override
  public int getItemViewType(int position) {
    if (collection.get(position).getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM10
        || collection.get(position).getReadingType().getEnumValue() == ReadingTypeEnum.AIR_PM2P5) {
      return RowType.EXTENDED_READING_ROW_TYPE;
    }

    return RowType.READING_ROW_TYPE;
  }

  @NotNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    this.context = parent.getContext();

    // if reading is not pm data inflate reading list row
    if (viewType == RowType.READING_ROW_TYPE) {
      View view = LayoutInflater.from(context).inflate(R.layout.reading_list_row, parent, false);
      return new ReadingHolder(view);
    }
    // if reading is pm data inflate extended reading list row
    else if (viewType == RowType.EXTENDED_READING_ROW_TYPE) {
      View view =
          LayoutInflater.from(context).inflate(R.layout.reading_extended_list_row, parent, false);
      return new ExtendedReadingHolder(view);
    } else {
      return null;
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    DeviceMeasurement deviceMeasurement = collection.get(position);

    // bind all but pm data
    if (holder instanceof ReadingHolder) {
      ((ReadingHolder) holder).bind(deviceMeasurement);
    }
    // bind pm data
    else if (holder instanceof ExtendedReadingHolder) {
      ((ExtendedReadingHolder) holder).bind(deviceMeasurement, context);
    }

    ImageView arrowImageView = holder.itemView.findViewById(R.id.tv_arrow_indicator);
    if (!deviceMeasurement.getName().equals("Motion")
        && !deviceMeasurement.getReadingType().getIdentifier().equals(ReadingTypeEnum.WATER_LEVEL_SWITCH.getIdentifier())) {
      if (arrowImageView != null) {
        arrowImageView.setVisibility(View.VISIBLE);
      }
      holder.itemView.setOnClickListener(c ->
          GraphActivity.show(holder.itemView.getContext(), deviceMeasurement, deviceId,  deviceId.equals("")));
    } else {
      holder.itemView.setOnClickListener(null);
      if (arrowImageView != null) {
        arrowImageView.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public int getItemCount() {
    return collection.size();
  }
}
