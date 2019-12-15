package com.kdh.chart.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
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
    public static final String LIST = "inputlist";
    private OnUpdateDataListener onUpdateDataListener;
    private ArrayList<SimpleInputRow> rowsList;
    private ListView rowsListView;
    private SimpleInputRowAdapter rowsAdapter;


    public SimpleInputFragment() {
    }

    public static SimpleInputFragment newInstance(ArrayList<SimpleInputRow> rowsList) {

        Bundle args = new Bundle();
        args.putSerializable(LIST, rowsList);
        SimpleInputFragment fragment = new SimpleInputFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ArrayList<SimpleInputRow> getRowsList() {
        return rowsList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        rowsListView = new ListView(getContext());
        registerForContextMenu(rowsListView);
        rowsListView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.onUpdateDataListener = (OnUpdateDataListener) getActivity();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = ((Activity) getContext()).getMenuInflater();
        menuInflater.inflate(R.menu.menu_row_context, menu);
        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContextItemSelected(item);
                return true;
            }
        };
        for (int i = 0; i < menu.size(); i++)
            menu.getItem(i).setOnMenuItemClickListener(listener);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (itemId) {
            case R.id.menu_delete:
                deleteRow(info.id);
                return true;
        }
        return super.onContextItemSelected(item);

    }

    private void deleteRow(long id) {
        if (id == 0) return;
        rowsList.remove((int) id);
        rowsAdapter.notifyDataSetChanged();
        onUpdateDataListener.onUpdate(rowsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.fragment_input_bottom_sheet, container, false);
        final Toolbar toolbar = mainView.findViewById(R.id.toolbar);
        final Button addRowButton = mainView.findViewById(R.id.btn_add_row);
        rowsList = (ArrayList<SimpleInputRow>) getArguments().getSerializable(LIST);
        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSimpleInputRowDialogFragment fragment = new AddSimpleInputRowDialogFragment();
                fragment.setOnPositiveButtonClickedListener(new AddSimpleInputRowDialogFragment.OnPositiveButtonClickedListener() {
                    @Override
                    public void onClick() {
                        drawSingleTableData(mainView);
                    }
                });
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
        //toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_up_white_24dp);
        drawSingleTableData(mainView);
        return mainView;
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

        rowsListView.setDivider(null);
        rowsListView.setDividerHeight(0);
        rowsListView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        rowsAdapter = new SimpleInputRowAdapter(context, R.layout.input_data_row, rowsList);
        rowsListView.setAdapter(rowsAdapter);
        //end list
        frameLayout.addView(rowsListView);
    }

    public interface OnUpdateDataListener {
        void onUpdate(ArrayList<SimpleInputRow> lists);
    }

}
