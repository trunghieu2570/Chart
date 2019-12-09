package com.kdh.chart.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kdh.chart.R;
import com.kdh.chart.adapters.AdvancedInputRowAdapter;
import com.kdh.chart.datatypes.AdvancedInputRow;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedInputFragment extends BottomSheetDialogFragment {


    public static final String TAG = "AdvancedInputFragment";
    public static final String LIST = "inputlist";
    private OnUpdateDataListener onUpdateDataListener;
    private ArrayList<AdvancedInputRow> rowsList;
    private ListView rowsListView;
    private AdvancedInputRowAdapter rowsAdapter;


    public AdvancedInputFragment() {
        // Required empty public constructor
    }

    public static AdvancedInputFragment newInstance(ArrayList<AdvancedInputRow> rowsList) {

        Bundle args = new Bundle();
        args.putSerializable(LIST, rowsList);
        AdvancedInputFragment fragment = new AdvancedInputFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ArrayList<AdvancedInputRow> getRowsList() {
        return rowsList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.onUpdateDataListener = (AdvancedInputFragment.OnUpdateDataListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.fragment_input_bottom_sheet, container, false);
        final Toolbar toolbar = mainView.findViewById(R.id.toolbar);
        final Button addRowButton = mainView.findViewById(R.id.btn_add_row);
        final Button addColumnButton = mainView.findViewById(R.id.btn_add_col);
        rowsList = (ArrayList<AdvancedInputRow>) getArguments().getSerializable(LIST);
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAdvancedInputRowDialogFragment fragment = AddAdvancedInputRowDialogFragment.newInstance();
                fragment.setOnPositiveButtonClickedListener(new AddAdvancedInputRowDialogFragment.OnPositiveButtonClickedListener() {
                    @Override
                    public void onClick() {
                        drawSingleTableData(mainView);
                    }
                });
                fragment.show(getFragmentManager(), "new row");
            }
        });
        addColumnButton.setVisibility(View.VISIBLE);
        addColumnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (AdvancedInputRow row : rowsList) {
                    row.getValues().add("");
                }
                drawSingleTableData(mainView);
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
        //toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_up_white_24dp);
        drawSingleTableData(mainView);
        return mainView;
    }


    private ArrayList<AdvancedInputRow> getData() {
        return rowsList;
    }

    private void drawSingleTableData(View view) {
        Context context = this.getContext();
        //frame
        FrameLayout frameLayout = view.findViewById(R.id.frame);
        frameLayout.removeAllViews();
        //scroll
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //list
        rowsListView = new ListView(context);
        rowsListView.setDivider(null);
        rowsListView.setDividerHeight(0);
        rowsListView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        rowsAdapter = new AdvancedInputRowAdapter(context, R.layout.advanced_input_data_row, rowsList);
        rowsListView.setAdapter(rowsAdapter);
        //end list
        horizontalScrollView.addView(rowsListView);
        frameLayout.addView(horizontalScrollView);
    }

    public interface OnUpdateDataListener {
        void onUpdate(ArrayList<AdvancedInputRow> lists);
    }

}
