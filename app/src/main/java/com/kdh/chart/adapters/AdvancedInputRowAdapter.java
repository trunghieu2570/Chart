package com.kdh.chart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.kdh.chart.datatypes.AdvancedInputRow;
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

    private void deleteColumnByIndex(int index) {
        if (mRows.get(0).getValues().size() == 1) return;
        for (AdvancedInputRow row : mRows) {
            row.getValues().remove(index);
        }
        notifyDataSetInvalidated();
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
                final EditText label = convertView.findViewById(R.id.label);
                final LinearLayout inputLayout = convertView.findViewById(R.id.input_layout);
                final LinearLayout layout = convertView.findViewById(R.id.data_row_layout);
                if (colorLabel != null) {
                    layout.removeView(colorLabel);
                }
                if (label != null) {
                    AdvancedInputRowAdapter.MyTextWatcher myTextWatcher = (AdvancedInputRowAdapter.MyTextWatcher) label.getTag();
                    if (myTextWatcher != null)
                        label.removeTextChangedListener(myTextWatcher);
                    label.setText(row.getLabel());
                    label.setInputType(InputType.TYPE_NULL);
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
                        final int index = i;
                        editText.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View view, int a, KeyEvent keyEvent) {
                                if (editText.getText().toString().equals("")
                                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL
                                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                    deleteColumnByIndex(index);
                                    return true;
                                }
                                return false;
                            }
                        });
                        //add new
                        final MyTextWatcher textWatcher = new AdvancedInputRowAdapter.MyTextWatcher(mRows.get(position), i);
                        editText.setTag(textWatcher);
                        editText.addTextChangedListener((AdvancedInputRowAdapter.MyTextWatcher) editText.getTag());
                        inputLayout.addView(editText);
                    }
                }
            }

        } else {
            final AdvancedInputRow row = mRows.get(position);
            if (row != null) {

                final TextView colorLabel = convertView.findViewById(R.id.color);
                final EditText label = convertView.findViewById(R.id.label);
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
                    AdvancedInputRowAdapter.MyTextWatcher myTextWatcher = (AdvancedInputRowAdapter.MyTextWatcher) label.getTag();
                    if (myTextWatcher != null)
                        label.removeTextChangedListener(myTextWatcher);
                    label.setText(row.getLabel());
                    //add new
                    label.setTag(new AdvancedInputRowAdapter.MyTextWatcher(mRows.get(position), -1));
                    label.addTextChangedListener((AdvancedInputRowAdapter.MyTextWatcher) label.getTag());
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
                        editText.setTag(new AdvancedInputRowAdapter.MyTextWatcher(mRows.get(position), i));
                        editText.addTextChangedListener((AdvancedInputRowAdapter.MyTextWatcher) editText.getTag());
                        inputLayout.addView(editText);
                    }
                }
            }
        }


        return convertView;
    }

    private class MyTextWatcher implements TextWatcher {
        private AdvancedInputRow mInputRow;
        private int id;

        private MyTextWatcher(AdvancedInputRow inputRow, int id) {
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
            if (id >= 0) {
                String value = str.equals("") ? "0" : str;
                mInputRow.getValues().set(id, value);
            } else {
                String label = str.equals("") ? "Item" : str;
                mInputRow.setLabel(label);
            }

        }

    }


}
