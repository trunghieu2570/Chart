package com.kdh.chart.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kdh.chart.R;
import com.kdh.chart.adapters.InputDataRowAdapter;
import com.kdh.chart.datatypes.InputDataRow;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputBottomSheetFragment extends BottomSheetDialogFragment {


    public static final String TAG = "InputBottomSheetFragment";

    public InputBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_bottom_sheet, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_input_table);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_up_white_24dp);
        drawSingleTableData(view);
        return view;
    }

    public static InputBottomSheetFragment newInstance() {
        Bundle args = new Bundle();

        InputBottomSheetFragment fragment = new InputBottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void drawTableData(View view) {
        Context context = this.getContext();
        String[] times = new String[]{"2011"};
        String[] fields = new String[]{"A", "B", "C"};
        FrameLayout frameLayout = view.findViewById(R.id.frame);
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TableLayout tableInput = new TableLayout(context);
        tableInput.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tableInput.removeAllViews();
        int column = times.length;
        int row = fields.length;

        //tạo bảng
        TableRow tableRow1 = new TableRow(context);
        tableRow1.removeAllViews();
        LinearLayout layout1 = new LinearLayout(context);
        layout1.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i <= column; i++) {
            TextView textView = new TextView(context);

            textView.setWidth(250);
            textView.setHeight(100);
            textView.setTextSize(20);

            textView.setTextColor(Color.WHITE);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(getResources().getDrawable(R.drawable.table_header_shape));
            if (i > 0) {
                textView.setText(times[i - 1]);
            }
            layout1.addView(textView);
        }
        tableRow1.addView(layout1);
        tableInput.addView(tableRow1);

        for (int i = 0; i < row; i++) {
            TableRow tableRow = new TableRow(context);
            LinearLayout layout = new LinearLayout(context);
            for (int j = 0; j <= column; j++) {
                if (j == 0) {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT));
                    textView.setTextSize(20);

                    textView.setTextColor(Color.WHITE);
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    layout.addView(textView);
                    textView.setText(fields[i]);
                    Drawable background = getResources().getDrawable(R.drawable.table_header_shape);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.RED);
                    gradientDrawable.setStroke(1, Color.parseColor("#BEBEBE"));
                    textView.setBackground(gradientDrawable);
                } else {
                    EditText editText = new EditText(context);

                    editText.setWidth(250);
                    editText.setMaxLines(1);

                    editText.setPadding(10, 10, 10, 10);
                    editText.setBackground(getResources().getDrawable(R.drawable.table_cell_shape));
                    layout.addView(editText);
                }
            }
            tableRow.addView(layout);
            tableInput.addView(tableRow);
        }

        horizontalScrollView.addView(tableInput);
        frameLayout.addView(horizontalScrollView);
    }

    private void drawSingleTableData(View view) {
        Context context = this.getContext();
        //rows
        String[] fields = new String[]{"A", "B", "C"};
        int row = fields.length;
        //frame
        FrameLayout frameLayout = view.findViewById(R.id.frame);
        //list
        ListView rowsListView = new ListView(context);
        rowsListView.setDivider(null);
        rowsListView.setDividerHeight(0);
        rowsListView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ArrayList<InputDataRow> rowsList = new ArrayList<>();
        for (String lb : fields) {
            rowsList.add(new InputDataRow(lb, Color.RED, ""));
        }
        InputDataRowAdapter rowsAdapter = new InputDataRowAdapter(context, R.layout.input_data_row, rowsList);
        rowsListView.setAdapter(rowsAdapter);
        //end list
        frameLayout.addView(rowsListView);
    }


}
