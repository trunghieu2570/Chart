package com.kdh.chart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kdh.chart.R;
import com.kdh.chart.activities.PieChartActivity;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.EditSimpleInputRowDialogFragment;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;

public class SimpleInputRowAdapter extends ArrayAdapter<SimpleInputRow> {
    private Context mContext;
    private ArrayList<SimpleInputRow> mRows;
    private int mResource;


    public SimpleInputRowAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SimpleInputRow> objects) {
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
            final TextView label = convertView.findViewById(R.id.label);
            final EditText editValue = convertView.findViewById(R.id.edit_value);
            if (colorLabel != null) {
                final GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setStroke(1, Color.parseColor("#BEBEBE"));
                gradientDrawable.setColor(row.getColor());
                colorLabel.setBackground(gradientDrawable);
                colorLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager manager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                        new SpectrumDialog.Builder(getContext())
                                .setColors(R.array.mdcolor_500)
                                .setSelectedColor(row.getColor())
                                .setDismissOnColorSelected(true)
                                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                                    @Override
                                    public void onColorSelected(boolean positiveResult, int color) {
                                        row.setColor(color);
                                        gradientDrawable.setColor(row.getColor());
                                        colorLabel.setBackground(gradientDrawable);
                                    }
                                })
                                .build()
                                .show(manager, "set color");

                    }
                });
            }
            if (label != null) {
                label.setText(row.getLabel());
                label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager manager = ((PieChartActivity)getContext()).getSupportFragmentManager();
                        EditSimpleInputRowDialogFragment fragment = EditSimpleInputRowDialogFragment.newInstance(position, row.getLabel(), row.getDescription());
                        fragment.setOnClickPositiveButtonListener(new EditSimpleInputRowDialogFragment.OnClickPositiveButtonListener() {
                            @Override
                            public void onClick() {
                                label.setText(row.getLabel());
                                notifyDataSetChanged();
                            }
                        });
                        fragment.setOnClickNeutralButtonListener(new EditSimpleInputRowDialogFragment.OnClickNeutralButtonListener() {
                            @Override
                            public void onClick() {
                                notifyDataSetChanged();
                            }
                        });
                        fragment.show(manager, "edit row");
                    }
                });
            }
            if (editValue != null) {
                //remove old
                MyTextWatcher myTextWatcher = (MyTextWatcher) editValue.getTag();
                if (myTextWatcher != null)
                    editValue.removeTextChangedListener(myTextWatcher);
                editValue.setText(row.getValue());
                //add new
                editValue.setTag(new MyTextWatcher(editValue, mRows.get(position)));
                editValue.addTextChangedListener((MyTextWatcher) editValue.getTag());
            }

        }
        return convertView;
    }

    private class MyTextWatcher implements TextWatcher {

        private EditText mEditText;
        private SimpleInputRow mInputRow;

        private MyTextWatcher(EditText editText, SimpleInputRow inputRow) {
            this.mEditText = editText;
            this.mInputRow = inputRow;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            String value = str.equals("") ? "0" : str;
            Log.d("DEBUG", "Change to" + value);
            mInputRow.setValue(value);
        }
    }
}
