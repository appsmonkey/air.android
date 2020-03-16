package io.cityos.cityosair.ui.main.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.reading.ReadingType;
import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;
import io.cityos.cityosair.data.model.schema.DeviceMeasurement;
import java.text.DecimalFormat;

public class ReadingHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.iv_reading_type) ImageView readingTypeImageView;
  @BindView(R.id.tv_reading_identifier) TextView readingIdentifierTextView;
  @BindView(R.id.tv_reading_value) TextView readingValueTextView;
  @BindView(R.id.tv_reading_notation) TextView readingNotationTextView;

  public ReadingHolder(View view) {
    super(view);
    ButterKnife.bind(this, view);
  }

  public void bind(DeviceMeasurement reading) {
    ReadingType readingType = reading.getReadingType();

    int drawable = readingType.getDrawable();
    readingTypeImageView.setImageResource(drawable);
    readingIdentifierTextView.setText(readingType.getEnumIdentifier());
    readingNotationTextView.setText(reading.getUnit());

    DecimalFormat decimalFormat = new DecimalFormat(reading.getReadingType()
        .getUnitNotation()
        .equals(ReadingTypeEnum.getUnitNotation(ReadingTypeEnum.AIR_TEMPERATURE)) ? "#.#" : "#");

    double value = reading.getValue();
    // if reading is pressure convert to hPa
    if (readingType.getIdentifier().equals(ReadingTypeEnum.AIR_PRESSURE.getIdentifier())) {
      value = value / 100;
      readingNotationTextView.setText(ReadingTypeEnum.getUnitNotation(ReadingTypeEnum.AIR_PRESSURE));
      readingValueTextView.setText(decimalFormat.format(Math.round(value)));
    }
    // if reading is from newly added sensor provide essential support to be shown
    else if (readingType.getIdentifier().equals(ReadingTypeEnum.UNIDENTIFIED.getIdentifier())) {
      readingIdentifierTextView.setText(reading.getName());
    }
    // we do not round battery voltage
    else if (readingType.getIdentifier().equals(ReadingTypeEnum.BATTERY_VOLTAGE.getIdentifier())) {
      readingValueTextView.setText(String.valueOf(value));
    } else {
      // values should already be rounded on backend just for case here as well
      readingValueTextView.setText(decimalFormat.format(Math.round(value)));
    }
  }
}
