package io.cityos.cityosair.ui.aqi;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import io.cityos.cityosair.R;
import io.cityos.cityosair.data.model.AqiIndex;
import java.util.List;

/**
 * Created by Andrej on 07/03/2017.
 */

public class AqiAdapter extends RecyclerView.Adapter<AqiAdapter.ViewHolder> {

  private List<AqiIndex> aqiList;
  private Context context;

  public AqiAdapter(List<AqiIndex> aqiList) {
    this.aqiList = aqiList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    this.context = parent.getContext();
    View view = LayoutInflater.from(context).inflate(R.layout.aqi_list_row, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    AqiIndex aqi = aqiList.get(position);

    GradientDrawable drawable = (GradientDrawable) holder.colorContainer.getBackground();
    drawable.setColor(ContextCompat.getColor(context, aqi.getBackgroundColorResource()));

    holder.aqiImageView.setImageResource(aqi.getImageResource());
    holder.aqiTextTextView.setText(aqi.getText());

    holder.aqiShortTextView.setText(aqi.getTitle());
    holder.aqiShortTextView.setTextColor(
        ContextCompat.getColor(context, aqi.getTextColorResource()));

    holder.aqiValueTextView.setText(aqi.getValue());
  }

  @Override
  public int getItemCount() {
    return aqiList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public final View view;
    public final LinearLayout colorContainer;
    public final TextView aqiValueTextView;
    public final TextView aqiShortTextView;
    public final TextView aqiTextTextView;
    public final ImageView aqiImageView;

    public ViewHolder(View view) {
      super(view);
      this.view = view;
      colorContainer = (LinearLayout) view.findViewById(R.id.color_container);
      aqiValueTextView = (TextView) view.findViewById(R.id.tv_aqi_value);
      aqiShortTextView = (TextView) view.findViewById(R.id.tv_aqi_short);
      aqiTextTextView = (TextView) view.findViewById(R.id.tv_aqi_text);
      aqiImageView = (ImageView) view.findViewById(R.id.iv_aqi_image);
    }
  }
}
