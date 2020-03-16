package io.cityos.cityosair.ui.graph;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.List;

import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.reading.ReadingType;
import io.cityos.cityosair.data.model.reading.ReadingTypeEnum;

/**
 * Created by Andrej on 12/03/2017.
 */

public class GraphMarker extends MarkerView {

    private Context context;
    private TextView tvReadingValue;
    private TextView tvReadingUnit;
    private TextView tvUpdateTime;
    private ReadingType tvReadingType;

    private List<String> timestamps;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public GraphMarker(Context context, int layoutResource, String notation, List<String> timestamps, LineChart chart, ReadingType readingType) {
        super(context, layoutResource);
        this.context = context;
        this.timestamps = timestamps;
        tvReadingValue = findViewById(R.id.tv_reading_value);
        tvReadingUnit = findViewById(R.id.tv_reading_unit);
        tvUpdateTime = findViewById(R.id.tv_update_time);
        tvReadingUnit.setText(notation);
        tvReadingType = readingType;
//        setChartView(chart);
    }

//    @Override
//    public void draw(Canvas canvas, float posX, float posY) {
//        super.draw(canvas, posX, posY);
//        getOffsetForDrawingAtPoint(posX, posY);
//    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        if (tvReadingType.getIdentifier().equals(ReadingTypeEnum.BATTERY_VOLTAGE.getIdentifier())) {
            decimalFormat = new DecimalFormat("#.##");
        }

        tvReadingValue.setText(decimalFormat.format(e.getY()));
        tvUpdateTime.setText(timestamps.get((int) e.getX()));

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }
}

