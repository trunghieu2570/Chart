package com.kdh.chart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kdh.chart.R;
import com.kdh.chart.activities.ProjectActivity;

import java.util.ArrayList;

public class ChartListViewAdapter extends ArrayAdapter<ProjectActivity.ChartTypeItem> {

    private Context context;
    private int resource;
    private ArrayList<ProjectActivity.ChartTypeItem> arrTypeChart;

    public ChartListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ProjectActivity.ChartTypeItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrTypeChart = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(this.resource, parent, false);
        }
        ProjectActivity.ChartTypeItem typeChart = arrTypeChart.get(position);
        if (typeChart != null) {
            ImageView imageView = convertView.findViewById(R.id.img);
            TextView chartName = convertView.findViewById(R.id.btnName);
            if (imageView != null)
                imageView.setImageResource(typeChart.getImageResource());
            if (chartName != null)
                chartName.setText(typeChart.getNameChart());
        }
        return convertView;
    }

}
