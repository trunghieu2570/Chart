package com.kdh.chart.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.kdh.chart.datatypes.Chart;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateLineChartDialogFragment extends DialogFragment {

    private Chart mChart;

    public static final String CHART_NAME = "chart_name";
    public static final String NUM_ROWS = "number_of_rows";
    public static final String NUM_COLS = "number_of_cols";
    public static final String CHART_TYPE = "chart_type";
    public static final String BUNDLE = "bundle";

    private EditText chartNameEdt;
    private EditText chartRowsEdt;
    private EditText chartColsEdt;
    private View view;


    public CreateLineChartDialogFragment() { }


    public static CreateLineChartDialogFragment newInstance(Chart chartType) {
        Bundle args = new Bundle();
        args.putSerializable("chart_type",chartType);
        CreateLineChartDialogFragment fragment = new CreateLineChartDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.fragment_create_line_chart_dialog, null, false);
        chartNameEdt = view.findViewById(R.id.edt_chart_name);
        chartRowsEdt = view.findViewById(R.id.edt_rows);
        chartColsEdt = view.findViewById(R.id.edt_cols);
        mChart = (Chart) getArguments().getSerializable("chart_type");
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Create new chart")
                .setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), ShowChartActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(CHART_NAME, chartNameEdt.getText().toString());
                        bundle.putInt(NUM_ROWS, Integer.parseInt("0" + chartRowsEdt.getText().toString()));
                        bundle.putInt(NUM_COLS, Integer.parseInt("0" + chartColsEdt.getText().toString()));
                        bundle.putSerializable(CHART_TYPE, mChart);
                        Log.d("DEBUG", "chart_name:" + chartNameEdt.getText().toString());
                        intent.putExtra(BUNDLE, bundle);
                        startActivity(intent);
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
