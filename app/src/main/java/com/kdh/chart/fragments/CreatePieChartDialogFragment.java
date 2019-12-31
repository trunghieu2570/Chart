package com.kdh.chart.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kdh.chart.FileManager;
import com.kdh.chart.R;
import com.kdh.chart.activities.PieChartActivity;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ChartTypeEnum;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.datatypes.SimpleInputRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePieChartDialogFragment extends DialogFragment {

    public static final String NUM_ROWS = "number_of_rows";
    public static final String BUNDLE = "bundle";
    public static final String PROJECT_LOCATION = "project";
    public static final String CHART = "chart";
    public static final String LOCATION = "location";
    public static final String NAME_LIST = "names_list";
    private OnChartNameDuplicatedListener onChartNameDuplicatedListener;

    public static CreatePieChartDialogFragment newInstance(ProjectLocation location) {
        ArrayList<Pair<ChartLocation, Chart>> charts = FileManager.loadCharts(location);
        ArrayList<String> projectNames = new ArrayList<>();
        if (charts != null) {
            for (Pair<ChartLocation, Chart> pair : charts) {
                projectNames.add(pair.second.getChartName());
            }
        }
        Bundle args = new Bundle();
        args.putSerializable(PROJECT_LOCATION, location);
        args.putStringArrayList(NAME_LIST, projectNames);
        CreatePieChartDialogFragment fragment = new CreatePieChartDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnChartNameDuplicatedListener(OnChartNameDuplicatedListener onChartNameDuplicatedListener) {
        this.onChartNameDuplicatedListener = onChartNameDuplicatedListener;
    }

    public CreatePieChartDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final ArrayList<String> namesList = getArguments().getStringArrayList(NAME_LIST);
        final ProjectLocation projectLocation = (ProjectLocation) getArguments().getSerializable(PROJECT_LOCATION);
        final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.fragment_create_pie_chart_dialog, null, false);
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
                        if (namesList.contains(chartNameEdt.getText().toString())) {
                            if (onChartNameDuplicatedListener != null)
                                onChartNameDuplicatedListener.onDuplicated();
                            return;
                        }
                        //init
                        final PieChart chart = new PieChart(
                                chartNameEdt.getText().toString(),
                                "Biểu đồ tròn"
                        );
                        //create new empty data;
                        final ArrayList<SimpleInputRow> inputRows = new ArrayList<>();
                        TypedArray colors = getResources().obtainTypedArray(R.array.mdcolor_500);
                        int color = getResources().getColor(R.color.blue_500);
                        //header
                        inputRows.add(new SimpleInputRow(groupNameEdt.getText().toString(), color, valueGroupNameEdt.getText().toString()));
                        final int numOfRows = Integer.parseInt("0" + chartRowsEdt.getText().toString());
                        for (int i = 0; i < numOfRows; i++) {
                            inputRows.add(new SimpleInputRow(groupNameEdt.getText().toString()+" " + (i+1), colors.getColor(i, color), ""));
                        }
                        colors.recycle();
                        chart.setData(inputRows);
                        //
                        final ChartLocation chartLocation = new ChartLocation(
                                UUID.randomUUID().toString() + ".json",
                                ChartTypeEnum.PIE
                        );
                        final Project project = projectLocation.getProject();
                        //modify
                        project.addChart(chartLocation);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", getResources().getConfiguration().locale);
                        project.setModifiedTime(dateFormat.format(Calendar.getInstance().getTime()));
                        //save data
                        FileManager.saveProject(projectLocation);
                        FileManager.saveChart(projectLocation, chart, chartLocation);
                        //pass bundle
                        final Intent intent = new Intent(getActivity(), PieChartActivity.class);
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

    public interface OnChartNameDuplicatedListener {
        void onDuplicated();
    }
}
