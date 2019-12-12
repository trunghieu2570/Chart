package com.kdh.chart.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.activities.DonutChartActivity;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ChartTypeEnum;
import com.kdh.chart.datatypes.DonutChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.datatypes.SimpleInputRow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


public class CreateDonutChartDialogFragment extends DialogFragment {

    public static final String NUM_ROWS = "number_of_rows";
    public static final String BUNDLE = "bundle";
    public static final String PROJECT_LOCATION = "project";
    public static final String CHART = "chart";
    public static final String LOCATION = "location";


    public CreateDonutChartDialogFragment() {
    }


    public static CreateDonutChartDialogFragment newInstance(ProjectLocation location) {
        Bundle args = new Bundle();
        args.putSerializable(PROJECT_LOCATION, location);
        CreateDonutChartDialogFragment fragment = new CreateDonutChartDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.fragment_create_donut_chart_dialog, null, false);
        final EditText chartNameEdt = view.findViewById(R.id.edt_chart_name);
        final EditText chartRowsEdt = view.findViewById(R.id.edt_rows);
        final EditText groupNameEdt = view.findViewById(R.id.edt_group_name);
        final EditText valueGroupNameEdt = view.findViewById(R.id.edt_value_group_name);
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle(R.string.create_chart)
                .setView(view)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int v) {
                        //init
                        final ProjectLocation projectLocation = (ProjectLocation) getArguments().getSerializable(PROJECT_LOCATION);
                        final DonutChart chart = new DonutChart(
                                chartNameEdt.getText().toString(),
                                "Biểu đồ donut"
                        );
                        //create new empty data;
                        final ArrayList<SimpleInputRow> inputRows = new ArrayList<>();
                        TypedArray colors = getResources().obtainTypedArray(R.array.mdcolor_500);
                        int color = getResources().getColor(R.color.blue_500);
                        //header
                        inputRows.add(new SimpleInputRow(groupNameEdt.getText().toString(), color, valueGroupNameEdt.getText().toString()));
                        final int numOfRows = Integer.parseInt("0" + chartRowsEdt.getText().toString());
                        for (int i = 0; i < numOfRows; i++) {
                            inputRows.add(new SimpleInputRow("Item " + i, colors.getColor(i, color), ""));
                        }
                        colors.recycle();
                        chart.setData(inputRows);
                        //
                        final ChartLocation chartLocation = new ChartLocation(
                                UUID.randomUUID().toString() + ".json",
                                ChartTypeEnum.DONUT
                        );
                        final Project project = projectLocation.getProject();
                        //modify
                        project.addChart(chartLocation);
                        project.setModifiedTime(Calendar.getInstance().getTime().toString());
                        //save data
                        ProjectFileManager.saveProject(projectLocation);
                        ProjectFileManager.saveChart(projectLocation, chart, chartLocation);
                        //pass bundle
                        final Intent intent = new Intent(getActivity(), DonutChartActivity.class);
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        intent.putExtra(BUNDLE, bundle);
                        startActivity(intent);
                        //end
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().dismiss();
                    }
                })
                .create();
    }
}
