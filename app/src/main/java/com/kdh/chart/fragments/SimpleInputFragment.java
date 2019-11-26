package com.kdh.chart.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kdh.chart.R;
import com.kdh.chart.adapters.SimpleInputRowAdapter;
import com.kdh.chart.datatypes.SimpleInputRow;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleInputFragment extends BottomSheetDialogFragment {


    public static final String TAG = "SimpleInputFragment";
    private OnUpdateDataListener onUpdateDataListener;
    private ArrayList<SimpleInputRow> rowsList;


    public SimpleInputFragment() {
        // Required empty public constructor
    }

    public SimpleInputFragment(ArrayList<SimpleInputRow> rowsList) {
        this.rowsList = rowsList;
    }

    public void setOnUpdateDataListener(OnUpdateDataListener onUpdateDataListener) {
        this.onUpdateDataListener = onUpdateDataListener;
    }

    public ArrayList<SimpleInputRow> getRowsList() {
        return rowsList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        if (rowsList == null)
            rowsList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_bottom_sheet, container, false);
        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        final Button addRowButton = view.findViewById(R.id.btn_add_row);
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSimpleInputRowDialogFragment fragment = new AddSimpleInputRowDialogFragment();
                fragment.show(getFragmentManager(), "new row");
            }
        });
        toolbar.inflateMenu(R.menu.menu_input_table);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_apply:
                        toolbar.requestFocus();
                        onUpdateDataListener.onUpdate(getData());
                        dismiss();
                        return true;
                }
                return false;
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_up_white_24dp);
        drawSingleTableData(view);
        return view;
    }


    private ArrayList<SimpleInputRow> getData() {
        return rowsList;
    }

    private void drawSingleTableData(View view) {
        Context context = this.getContext();
        //frame
        FrameLayout frameLayout = view.findViewById(R.id.frame);
        frameLayout.removeAllViews();
        //list
        ListView rowsListView = new ListView(context);
        rowsListView.setDivider(null);
        rowsListView.setDividerHeight(0);
        rowsListView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        SimpleInputRowAdapter rowsAdapter = new SimpleInputRowAdapter(context, R.layout.input_data_row, rowsList);
        rowsListView.setAdapter(rowsAdapter);
        //end list
        frameLayout.addView(rowsListView);
    }

    public interface OnUpdateDataListener {
        void onUpdate(ArrayList<SimpleInputRow> lists);
    }

}
