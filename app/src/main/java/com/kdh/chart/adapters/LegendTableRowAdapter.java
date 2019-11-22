package com.kdh.chart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kdh.chart.R;
import com.kdh.chart.datatypes.SimpleInputRow;

import java.util.ArrayList;

public class LegendTableRowAdapter extends ArrayAdapter<SimpleInputRow> {
    private Context mContext;
    private ArrayList<SimpleInputRow> mRows;
    private int mResource;


    public LegendTableRowAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SimpleInputRow> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mRows = objects;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }
        final SimpleInputRow row = mRows.get(position);
        if (row != null) {
            final TextView colorLabel = convertView.findViewById(R.id.color);
            final TextView description = convertView.findViewById(R.id.description);
            if (colorLabel != null) {
                final GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#BEBEBE"));
                gradientDrawable.setColor(row.getColor());
                colorLabel.setBackground(gradientDrawable);
            }
            if (description != null) {
                description.setText(row.getDescription());
            }

        }
        return convertView;
    }
}
