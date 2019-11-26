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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.activities.ProjectActivity;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;

import java.util.Calendar;

public class CreateProjectDialogFragment extends DialogFragment {


    public static final String BUNDLE = "bundle";
    public static final String PROJECT_LOCATION = "project";

    private EditText projectNameEdt;
    private EditText chartFieldsEdt;
    private View view;

    public CreateProjectDialogFragment() {
        // Required empty public constructor
    }

    public static CreateProjectDialogFragment newInstance() {
        Bundle args = new Bundle();
        CreateProjectDialogFragment fragment = new CreateProjectDialogFragment();
        fragment.setArguments(args);
        return fragment;
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
                        final Project project = new Project(projectNameEdt.getText().toString(), Calendar.getInstance().getTime().toString());
                        final ProjectLocation projectLocation = new ProjectLocation(
                                ProjectFileManager.makeDefaultProjectPath(project).toString(),
                                project
                        );
                        ProjectFileManager.saveProject(projectLocation);

                        Intent intent = new Intent(getActivity(), ProjectActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        //bundle.putString(PROJECT_NAME, projectNameEdt.getText().toString());
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
