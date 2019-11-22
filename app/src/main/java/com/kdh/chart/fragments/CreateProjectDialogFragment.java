package com.kdh.chart.fragments;


import android.app.Activity;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kdh.chart.R;
import com.kdh.chart.activities.ProjectActivity;
import com.kdh.chart.datatypes.ChartTypeItem;

public class CreateProjectDialogFragment extends DialogFragment {

    private ChartTypeItem mChartTypeItem;
    private Activity mActivity;

    public static final String PROJECT_NAME = "prjname";
    public static final String BUNDLE = "bundle";

    private EditText projectNameEdt;
    private EditText chartFieldsEdt;
    private View view;

    public CreateProjectDialogFragment() {
        // Required empty public constructor
    }

    public CreateProjectDialogFragment(Activity activity, ChartTypeItem chartTypeItem) {
        this.mChartTypeItem = chartTypeItem;
        this.mActivity = activity;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.fragment_create_project_dialog, null, false);
        projectNameEdt = view.findViewById(R.id.edt_project_name);
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Enter project name")
                .setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        projectNameEdt = view.findViewById(R.id.edt_project_name);
                        Intent intent = new Intent(mActivity, ProjectActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(PROJECT_NAME, projectNameEdt.getText().toString());
                        Log.d("DEBUG", "Project_name:" + projectNameEdt.getText().toString());
                        intent.putExtra(BUNDLE, bundle);
                        startActivity(intent);
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: do something else
                    }
                })
                .create();
    }
}
