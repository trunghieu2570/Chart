package com.kdh.chart.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kdh.chart.R;
import com.kdh.chart.activities.ShowChartActivity;
import com.kdh.chart.datatypes.AdvancedInputRow;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddAdvancedInputRowDialogFragment extends DialogFragment {

    private View view;
    private EditText rowNameEdt;
    private EditText descriptionEdt;
    private OnPositiveButtonClickedListener onPositiveButtonClickedListener;


    public AddAdvancedInputRowDialogFragment() {
        // Required empty public constructor
    }

    public static AddAdvancedInputRowDialogFragment newInstance(int numOfColumns) {

        Bundle args = new Bundle();
        args.putInt("numcolumns", numOfColumns);
        AddAdvancedInputRowDialogFragment fragment = new AddAdvancedInputRowDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.fragment_add_new_row_dialog, null, false);
        rowNameEdt = view.findViewById(R.id.edt_row_name);
        descriptionEdt = view.findViewById(R.id.edt_row_description);
        final int numOfColumns = getArguments().getInt("numcolumns");
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Add new row")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final int rows = ((ShowChartActivity) getActivity()).getAdvancedInputRows().get(0).getValues().size();
                        ArrayList<String> values = new ArrayList<>();
                        for (int j = 0; j < (rows == 0 ? numOfColumns : rows); j++)
                            values.add("");
                        AdvancedInputRow row = new AdvancedInputRow(
                                rowNameEdt.getText().toString(),
                                R.color.blue_500,
                                values,
                                descriptionEdt.getText().toString());

                        ((ShowChartActivity) getActivity()).getAdvancedInputRows().add(row);
                        onPositiveButtonClickedListener.onClick();
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().dismiss();
                    }
                })
                .create();
    }

    public void setOnPositiveButtonClickedListener(OnPositiveButtonClickedListener onPositiveButtonClickedListener) {
        this.onPositiveButtonClickedListener = onPositiveButtonClickedListener;
    }

    public interface OnPositiveButtonClickedListener {
        void onClick();
    }
}
