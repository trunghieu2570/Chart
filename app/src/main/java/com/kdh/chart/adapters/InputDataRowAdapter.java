package com.kdh.chart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kdh.chart.R;
import com.kdh.chart.datatypes.InputDataRow;

import java.util.ArrayList;

public class InputDataRowAdapter extends ArrayAdapter<InputDataRow> {
    private Context mContext;
    private ArrayList<InputDataRow> mRows;
    private int mResource;

    public InputDataRowAdapter(@NonNull Context context, int resource, @NonNull ArrayList<InputDataRow> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mRows = objects;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }
        InputDataRow row = mRows.get(position);
        if (row != null) {
            TextView labelTextView = convertView.findViewById(R.id.label);
            EditText editValue = convertView.findViewById(R.id.edit_value);
            if (labelTextView != null) {
                labelTextView.setText(row.getLabel());
            }
            if (editValue != null)
                editValue.setText(row.getValue().toString());
        }
        return convertView;
    }
}
