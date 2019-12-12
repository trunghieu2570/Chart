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
import com.kdh.chart.activities.ChartActivityInterface;
import com.kdh.chart.activities.LineChartActivity;
import com.kdh.chart.datatypes.AdvancedInputRow;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditAdvancedInputRowDialogFragment extends DialogFragment {

    private View view;
    private EditText rowNameEdt;
    private OnClickPositiveButtonListener onClickPositiveButtonListener;
    private OnClickNeutralButtonListener onClickNeutralButtonListener;


    public EditAdvancedInputRowDialogFragment() {
        // Required empty public constructor
    }

    public static EditAdvancedInputRowDialogFragment newInstance(int item, String s1) {
        Bundle args = new Bundle();
        args.putInt("item", item);
        args.putCharSequence("name", s1);
        EditAdvancedInputRowDialogFragment fragment = new EditAdvancedInputRowDialogFragment();
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
        final int item = getArguments().getInt("item");
        String name = getArguments().getCharSequence("name").toString();
        rowNameEdt.setText(name);
        return new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle(R.string.edit_item)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AdvancedInputRow row = ((ChartActivityInterface<AdvancedInputRow>) getContext()).getInputRows().get(item);
                        row.setLabel(rowNameEdt.getText().toString());
                        onClickPositiveButtonListener.onClick();
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getDialog().dismiss();
                    }
                })
                .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ChartActivityInterface<AdvancedInputRow>) getContext()).getInputRows().remove(item);
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
