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
import com.kdh.chart.FileManager;
import com.kdh.chart.R;
import com.kdh.chart.activities.ProjectActivity;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateProjectDialogFragment extends DialogFragment {


    public static final String BUNDLE = "bundle";
    public static final String PROJECT_LOCATION = "project";
    public static final String NAME_LIST = "names_list";
    private EditText projectNameEdt;
    private EditText chartFieldsEdt;
    private OnProjectNameDuplicatedListener onProjectNameDuplicatedListener;


    public CreateProjectDialogFragment() {
        // Required empty public constructor
    }

    public static CreateProjectDialogFragment newInstance(ArrayList<ProjectLocation> projectLocations) {
        ArrayList<String> projectNames = new ArrayList<>();
        for (ProjectLocation projectLocation : projectLocations) {
            projectNames.add(projectLocation.getProject().getName());
        }
        Bundle args = new Bundle();
        args.putStringArrayList(NAME_LIST, projectNames);
        CreateProjectDialogFragment fragment = new CreateProjectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final ArrayList<String> namesList = getArguments().getStringArrayList(NAME_LIST);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.fragment_create_project_dialog, null, false);
        projectNameEdt = view.findViewById(R.id.edt_project_name);
        return new MaterialAlertDialogBuilder(getActivity())
                .setTitle(R.string.create_project_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (namesList.contains(projectNameEdt.getText().toString())) {
                            if (onProjectNameDuplicatedListener != null)
                                onProjectNameDuplicatedListener.onDuplicated();
                            return;
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", getResources().getConfiguration().locale);
                        final Project project = new Project(projectNameEdt.getText().toString(), dateFormat.format(Calendar.getInstance().getTime()));
                        final ProjectLocation projectLocation = new ProjectLocation(
                                FileManager.makeDefaultProjectPath(project).toString(),
                                project
                        );
                        FileManager.saveProject(projectLocation);

                        Intent intent = new Intent(getActivity(), ProjectActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        //bundle.putString(PROJECT_NAME, projectNameEdt.getText().toString());
                        intent.putExtra(BUNDLE, bundle);
                        startActivity(intent);
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

    public void setOnProjectNameDuplicatedListener(OnProjectNameDuplicatedListener onProjectNameDuplicatedListener) {
        this.onProjectNameDuplicatedListener = onProjectNameDuplicatedListener;
    }

    public interface OnProjectNameDuplicatedListener {
        void onDuplicated();
    }

}
