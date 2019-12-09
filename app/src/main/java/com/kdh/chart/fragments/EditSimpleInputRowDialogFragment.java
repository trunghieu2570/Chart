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
public class EditSimpleInputRowDialogFragment extends DialogFragment {

    private View view;
    private EditText rowNameEdt;
    private EditText descriptionEdt;
    private OnClickPositiveButtonListener onClickPositiveButtonListener;
    private OnClickNeutralButtonListener onClickNeutralButtonListener;


    public EditSimpleInputRowDialogFragment() {
        // Required empty public constructor
    }

    public static EditSimpleInputRowDialogFragment newInstance(int item, String s1, String s2) {
        Bundle args = new Bundle();
        args.putInt("item", item);
        args.putCharSequence("name", s1);
        args.putCharSequence("description", s2);
        EditSimpleInputRowDialogFragment fragment = new EditSimpleInputRowDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.fragment_add_new_row_dialog, null, false);
        rowNameEdt = view.findViewById(R.id.edt_row_name);
        descriptionEdt = view.findViewById(R.id.edt_row_description);
        final int item = getArguments().getInt("item");
        String name = getArguments().getCharSequence("name").toString();
        String description = getArguments().getCharSequence("description").toString();
        rowNameEdt.setText(name);
        descriptionEdt.setText(description);
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Edit row")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SimpleInputRow row = ((PieChartActivity) getContext()).getInputRows().get(item);
                        row.setLabel(rowNameEdt.getText().toString());
                        row.setDescription(descriptionEdt.getText().toString());
                        onClickPositiveButtonListener.onClick();
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().dismiss();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((PieChartActivity) getContext()).getInputRows().remove(item);
                        onClickNeutralButtonListener.onClick();
                        getDialog().dismiss();
                    }
                })
                .create();
    }
    public interface OnClickPositiveButtonListener {
        void onClick();
    }
    public interface OnClickNeutralButtonListener {
        void onClick();
    }

    public void setOnClickPositiveButtonListener(OnClickPositiveButtonListener onClickPositiveButtonListener) {
        this.onClickPositiveButtonListener = onClickPositiveButtonListener;
    }

    public void setOnClickNeutralButtonListener(OnClickNeutralButtonListener onClickNeutralButtonListener) {
        this.onClickNeutralButtonListener = onClickNeutralButtonListener;
    }
}