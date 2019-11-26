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
import com.kdh.chart.activities.PieChartActivity;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ChartTypes;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.Project;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePieChartDialogFragment extends DialogFragment {

    public static final String NUM_ROWS = "number_of_rows";
    public static final String BUNDLE = "bundle";
    public static final String PROJECT = "project";
    public static final String CHART = "chart";
    public static final String LOCATION = "location";


    public CreatePieChartDialogFragment() { }


    public static CreatePieChartDialogFragment newInstance(Project project) {
        Bundle args = new Bundle();
        args.putSerializable(PROJECT, project);
        CreatePieChartDialogFragment fragment = new CreatePieChartDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.fragment_create_pie_chart_dialog, null, false);
        final EditText chartNameEdt = view.findViewById(R.id.edt_chart_name);
        final EditText chartRowsEdt = view.findViewById(R.id.edt_rows);
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Create new chart")
                .setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //init
                        final Project project = (Project) getArguments().getSerializable(PROJECT);
                        final PieChart chart = new PieChart(
                                chartNameEdt.getText().toString(),
                                "This is a PieChart",
                                Calendar.getInstance().getTime().toString()
                        );
                        final ChartLocation chartLocation = new ChartLocation(
                                chart.getChartName().replace(' ', '_') + ".json",
                                ChartTypes.PIE
                        );
                        //modify
                        project.addChart(chartLocation);
                        project.setModifiedTime(Calendar.getInstance().getTime().toString());
                        //save data
                        ProjectFileManager.saveProject(project);
                        ProjectFileManager.saveChart(project, chart, chartLocation);
                        //pass bundle
                        final Intent intent = new Intent(getActivity(), PieChartActivity.class);
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable(PROJECT, project);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        bundle.putInt(NUM_ROWS, Integer.parseInt("0" + chartRowsEdt.getText().toString()));
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
