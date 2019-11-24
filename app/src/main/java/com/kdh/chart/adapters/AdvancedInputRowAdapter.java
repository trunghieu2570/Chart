package com.kdh.chart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kdh.chart.R;
import com.kdh.chart.activities.ShowChartActivity;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.fragments.EditAdvancedInputRowDialogFragment;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class AdvancedInputRowAdapter extends ArrayAdapter<AdvancedInputRow> {
    private Context mContext;
    private ArrayList<AdvancedInputRow> mRows;
    private int mResource;


    public AdvancedInputRowAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AdvancedInputRow> objects) {
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
        if (position == 0) {
            final AdvancedInputRow row = mRows.get(position);
            if (row != null) {
                final TextView colorLabel = convertView.findViewById(R.id.color);
                final TextView label = convertView.findViewById(R.id.label);
                final LinearLayout inputLayout = convertView.findViewById(R.id.input_layout);
                final LinearLayout layout = convertView.findViewById(R.id.data_row_layout);
                if (colorLabel != null) {
                    layout.removeView(colorLabel);
                }
                if (label != null) {
                    label.setText(row.getLabel());
                    label.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.table_header_width), ViewGroup.LayoutParams.MATCH_PARENT));
                    label.setBackgroundResource(R.drawable.table_header_shape);
                    label.setOnClickListener(null);
                }
                if (inputLayout != null) {
                    inputLayout.removeAllViews();
                    for (int i = 0; i < row.getValues().size(); i++) {
                        final EditText editText = new EditText(mContext);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                        editText.setPadding(10, 10, 10, 10);
                        editText.setText(row.getValues().get(i));
                        editText.setBackgroundResource(R.drawable.table_header_shape);
                        AdvancedInputRowAdapter.MyTextWatcher myTextWatcher = (AdvancedInputRowAdapter.MyTextWatcher) editText.getTag();
                        if (myTextWatcher != null)
                            editText.removeTextChangedListener(myTextWatcher);
                        editText.setText(row.getValues().get(i));
                        //add new
                        editText.setTag(new AdvancedInputRowAdapter.MyTextWatcher(editText, mRows.get(position), i));
                        editText.addTextChangedListener((AdvancedInputRowAdapter.MyTextWatcher) editText.getTag());
                        inputLayout.addView(editText);
                    }
                }
            }

        } else {
            final AdvancedInputRow row = mRows.get(position);
            if (row != null) {

                final TextView colorLabel = convertView.findViewById(R.id.color);
                final TextView label = convertView.findViewById(R.id.label);
                final LinearLayout inputLayout = convertView.findViewById(R.id.input_layout);
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
                            FragmentManager manager = ((ShowChartActivity) getContext()).getSupportFragmentManager();
                            EditAdvancedInputRowDialogFragment fragment = EditAdvancedInputRowDialogFragment.newInstance(position, row.getLabel(), row.getDescription());
                            fragment.setOnClickPositiveButtonListener(new EditAdvancedInputRowDialogFragment.OnClickPositiveButtonListener() {
                                @Override
                                public void onClick() {
                                    label.setText(row.getLabel());
                                    notifyDataSetChanged();
                                }
                            });
                            fragment.setOnClickNeutralButtonListener(new EditAdvancedInputRowDialogFragment.OnClickNeutralButtonListener() {
                                @Override
                                public void onClick() {
                                    notifyDataSetChanged();
                                }
                            });
                            fragment.show(manager, "edit row");
                        }
                    });
                }
                if (inputLayout != null) {
                    inputLayout.removeAllViews();
                    for (int i = 0; i < row.getValues().size(); i++) {
                        final EditText editText = new EditText(mContext);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT));
                        editText.setPadding(10, 10, 10, 10);
                        editText.setText(row.getValues().get(i));
                        editText.setBackgroundResource(R.drawable.table_cell_shape);
                        editText.setHint("0");
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
                        editText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                        AdvancedInputRowAdapter.MyTextWatcher myTextWatcher = (AdvancedInputRowAdapter.MyTextWatcher) editText.getTag();
                        if (myTextWatcher != null)
                            editText.removeTextChangedListener(myTextWatcher);
                        editText.setText(row.getValues().get(i));
                        //add new
                        editText.setTag(new AdvancedInputRowAdapter.MyTextWatcher(editText, mRows.get(position), i));
                        editText.addTextChangedListener((AdvancedInputRowAdapter.MyTextWatcher) editText.getTag());
                        inputLayout.addView(editText);
                    }
                }
            }
        }


        return convertView;
    }
    private class MyTextWatcher implements TextWatcher {

        private EditText mEditText;
        private AdvancedInputRow mInputRow;
        private int id;

        private MyTextWatcher(EditText editText, AdvancedInputRow inputRow, int id) {
            this.mEditText = editText;
            this.mInputRow = inputRow;
            this.id = id;
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
            mInputRow.getValues().set(id, value);
        }
    }
}
