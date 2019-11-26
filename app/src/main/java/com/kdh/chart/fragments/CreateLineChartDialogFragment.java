package com.kdh.chart.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.activities.LineChartActivity;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ChartTypeEnum;
import com.kdh.chart.datatypes.LineChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateLineChartDialogFragment extends DialogFragment {

    public static final String CHART_NAME = "chart_name";
    public static final String NUM_ROWS = "number_of_rows";
    public static final String NUM_COLS = "number_of_cols";
    public static final String CHART = "chart";
    public static final String BUNDLE = "bundle";
    public static final String PROJECT_LOCATION = "project";
    public static final String LOCATION = "location";



    public CreateLineChartDialogFragment() { }


    public static CreateLineChartDialogFragment newInstance(ProjectLocation location) {
        Bundle args = new Bundle();
        args.putSerializable(PROJECT_LOCATION, location);
        CreateLineChartDialogFragment fragment = new CreateLineChartDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.fragment_create_line_chart_dialog, null, false);
        final EditText chartNameEdt = view.findViewById(R.id.edt_chart_name);
        final EditText chartRowsEdt = view.findViewById(R.id.edt_rows);
        final EditText chartColsEdt = view.findViewById(R.id.edt_cols);
        final EditText xAxisUnitEdt = view.findViewById(R.id.edt_x_axis_unit);
        final EditText yAxisUnitEdt = view.findViewById(R.id.edt_y_axis_unit);
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Create new chart")
                .setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //init
                        final ProjectLocation projectLocation = (ProjectLocation) getArguments().getSerializable(PROJECT_LOCATION);
                        final LineChart chart = new LineChart(
                                chartNameEdt.getText().toString(),
                                "This is a LineChart",
                                Calendar.getInstance().getTime().toString(),
                                xAxisUnitEdt.getText().toString(),
                                yAxisUnitEdt.getText().toString()
                        );
                        final ChartLocation chartLocation = new ChartLocation(
                                chart.getChartName().replace(' ', '_') + ".json",
                                ChartTypeEnum.LINE
                        );
                        final Project project = projectLocation.getProject();
                        //modify
                        project.addChart(chartLocation);
                        project.setModifiedTime(Calendar.getInstance().getTime().toString());
                        //save data
                        ProjectFileManager.saveProject(projectLocation);
                        ProjectFileManager.saveChart(projectLocation, chart, chartLocation);
                        //pass data
                        Intent intent = new Intent(getActivity(), LineChartActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(NUM_ROWS, Integer.parseInt("0" + chartRowsEdt.getText().toString()));
                        bundle.putInt(NUM_COLS, Integer.parseInt("0" + chartColsEdt.getText().toString()));
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        intent.putExtra(BUNDLE, bundle);
                        startActivity(intent);
                        //end
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
