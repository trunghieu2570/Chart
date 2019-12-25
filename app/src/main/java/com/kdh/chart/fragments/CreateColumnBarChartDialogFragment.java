package com.kdh.chart.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.activities.ColumnBarChartActivity;
import com.kdh.chart.activities.GroupBarChartActivity;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ChartTypeEnum;
import com.kdh.chart.datatypes.ColumnBarChart;
import com.kdh.chart.datatypes.GroupBarChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;



public class CreateColumnBarChartDialogFragment extends DialogFragment {

    public static final String NUM_COLS = "number_of_cols";
    public static final String CHART = "chart";
    public static final String BUNDLE = "bundle";
    public static final String PROJECT_LOCATION = "project";
    public static final String LOCATION = "location";

    public CreateColumnBarChartDialogFragment() { }


    public static CreateColumnBarChartDialogFragment newInstance(ProjectLocation location) {
        Bundle args = new Bundle();
        args.putSerializable(PROJECT_LOCATION, location);
        CreateColumnBarChartDialogFragment fragment = new CreateColumnBarChartDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.fragment_create_column_bar_chart_dialog, null, false);
        final EditText chartNameEdt = view.findViewById(R.id.edt_chart_name);
        final EditText xUnitEdt = view.findViewById(R.id.edt_x_axis_unit);
        final EditText yUnitEdt = view.findViewById(R.id.edt_y_axis_unit);
        final EditText yUnitMeaningEdt = view.findViewById(R.id.edt_y_axis_unit_meaning);
        final EditText objMeaningEdt = view.findViewById(R.id.edt_object_meaning);
        final EditText chartRowsEdt = view.findViewById(R.id.edt_rows);
        final EditText chartColsEdt = view.findViewById(R.id.edt_cols);
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle(R.string.create_chart)
                .setView(view)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int v) {
                        //init
                        final ProjectLocation projectLocation = (ProjectLocation) getArguments().getSerializable(PROJECT_LOCATION);
                        final ColumnBarChart chart = new ColumnBarChart(
                                chartNameEdt.getText().toString(),
                                "Biểu đồ column",
                                xUnitEdt.getText().toString(),
                                yUnitEdt.getText().toString(),
                                yUnitMeaningEdt.getText().toString(),
                                objMeaningEdt.getText().toString()
                        );
                        final ArrayList<AdvancedInputRow> inputRows = new ArrayList<>();
                        final int numOfRows = Integer.parseInt("0" + chartRowsEdt.getText().toString());
                        final int numOfCols = Integer.parseInt("0" + chartColsEdt.getText().toString());
                        TypedArray colors = getResources().obtainTypedArray(R.array.mdcolor_500);
                        int color = getResources().getColor(R.color.blue_500);

                        //header
                        ArrayList<String> values = new ArrayList<>();
                        for (int j = 0; j < numOfCols; j++)
                            values.add("C" + j);
                        inputRows.add(new AdvancedInputRow(getString(R.string.data_table), color, values));
                        //content
                        for (int i = 0; i < numOfRows; i++) {
                            color = getResources().getColor(R.color.blue_500);
                            values = new ArrayList<>();
                            for (int j = 0; j < numOfCols; j++)
                                values.add("");
                            inputRows.add(new AdvancedInputRow("Item" + i, colors.getColor(i, color), values));
                        }
                        colors.recycle();
                        chart.setData(inputRows);

                        final ChartLocation chartLocation = new ChartLocation(
                                UUID.randomUUID().toString() + ".json",
                                ChartTypeEnum.COLUMN
                        );
                        final Project project = projectLocation.getProject();
                        //modify
                        project.addChart(chartLocation);
                        project.setModifiedTime(Calendar.getInstance().getTime().toString());
                        //save data
                        ProjectFileManager.saveProject(projectLocation);
                        ProjectFileManager.saveChart(projectLocation, chart, chartLocation);
                        //pass data

                        Intent intent = new Intent(getActivity(), ColumnBarChartActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        intent.putExtra(BUNDLE, bundle);
                        startActivity(intent);
                        System.out.println("131219");
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