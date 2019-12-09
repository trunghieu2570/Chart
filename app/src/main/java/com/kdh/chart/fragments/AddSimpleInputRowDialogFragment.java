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
import com.kdh.chart.activities.PieChartActivity;
import com.kdh.chart.datatypes.SimpleInputRow;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddSimpleInputRowDialogFragment extends DialogFragment {

    private View view;
    private EditText rowNameEdt;
    private EditText descriptionEdt;


    public AddSimpleInputRowDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.fragment_add_new_row_dialog, null, false);
        rowNameEdt = view.findViewById(R.id.edt_row_name);
        descriptionEdt = view.findViewById(R.id.edt_row_description);

        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Add new row")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleInputRow row = new SimpleInputRow(
                                rowNameEdt.getText().toString(),
                                R.color.blue_500,
                                "",
                                descriptionEdt.getText().toString());

                        ((PieChartActivity) getActivity()).getInputRows().add(row);
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
}