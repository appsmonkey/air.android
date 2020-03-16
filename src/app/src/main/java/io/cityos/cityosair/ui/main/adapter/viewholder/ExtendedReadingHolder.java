package io.cityos.cityosair.ui.main.adapter.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.AqiIndex;
import io.cityos.cityosair.data.model.reading.ReadingType;
import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;
import io.cityos.cityosair.data.model.schema.DeviceMeasurement;
import io.cityos.cityosair.ui.aqi.AQIEnum;
import java.text.DecimalFormat;

public class ExtendedReadingHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.iv_reading_type) ImageView readingTypeImageView;
  @BindView(R.id.tv_reading_identifier) TextView readingIdentifierTextView;
  @BindView(R.id.tv_reading_value) TextView readingValueTextView;
  @BindView(R.id.tv_reading_notation) TextView readingNotationTextView;
  @BindView(R.id.tv_reading_subtitle) TextView readingSubtitleTextView;

  public ExtendedReadingHolder(View view) {
    super(view);
    ButterKnife.bind(this, view);
  }

  public void bind(DeviceMeasurement reading, Context context) {
    ReadingType readingType = reading.getReadingType();
    readingIdentifierTextView.setText(readingType.getEnumIdentifier());
    readingNotationTextView.setText(reading.getUnit());

    DecimalFormat decimalFormat = new DecimalFormat("#");

    readingValueTextView.setText(decimalFormat.format(reading.getValue()));

    AQIEnum aqiType = readingType.getEnumValue() == ReadingTypeEnum.AIR_PM10 ? AQIEnum.AIR_PM10 : AQIEnum.AIR_PM2P5;
    AqiIndex aqi = AqiIndex.getAQIForTypeWithValue(reading.getValue(), aqiType);

    readingSubtitleTextView.setText(reading.getAverage());

    readingTypeImageView.setImageResource(aqi.getImageResource());
    readingValueTextView.setTextColor(ContextCompat.getColor(context, aqi.getTextColorResource()));
    readingSubtitleTextView.setTextColor(ContextCompat.getColor(context, aqi.getTextColorResource()));
  }
}
