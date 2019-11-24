package com.kdh.chart.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.R;
import com.kdh.chart.charts.ChartView;
import com.kdh.chart.charts.LineChart;
import com.kdh.chart.charts.PieChart;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.AdvancedInputFragment;
import com.kdh.chart.fragments.CreateChartDialogFragment;
import com.kdh.chart.fragments.CreateLineChartDialogFragment;
import com.kdh.chart.fragments.SimpleInputFragment;

import java.util.ArrayList;

public class ShowChartActivity extends AppCompatActivity {

    private ChartView mChartView;
    private DialogFragment mInputTable;
    private ArrayList<SimpleInputRow> simpleInputRows;
    private ArrayList<AdvancedInputRow> advancedInputRows;
    private ListView legendTableListView;
    private String chartName;
    private int numOfRows;
    private int numOfCols;
    private Chart chartType;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        final Bundle bundle = getIntent().getBundleExtra(CreateChartDialogFragment.BUNDLE);
        chartName = bundle.getString(CreateChartDialogFragment.CHART_NAME);
        numOfRows = bundle.getInt(CreateChartDialogFragment.NUM_ROWS);
        numOfCols = bundle.getInt(CreateLineChartDialogFragment.NUM_COLS);
        chartType = (Chart) bundle.getSerializable(CreateChartDialogFragment.CHART_TYPE);
        if (chartName != null)
            setTitle(chartName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        layout = findViewById(R.id.layout);
        switch (chartType.getType()) {
            case PIE:
                showPieChart();
                break;
            case LINE:
                showLineChart();
                break;
        }
    }

    private void showPieChart() {
        simpleInputRows = new ArrayList<>();
        TypedArray colors = getResources().obtainTypedArray(R.array.mdcolor_500);
        for (int i = 0; i < numOfRows; i++) {
            int color = getResources().getColor(R.color.blue_500);
            simpleInputRows.add(new SimpleInputRow("Tỉ lệ" + i, colors.getColor(i, color), "", "tỉ trọng " + i));
        }
        colors.recycle();
        mInputTable = new SimpleInputFragment(simpleInputRows);
        layout.removeAllViews();
        mChartView = new PieChart(this, null);
        layout.addView((View) mChartView);
        ((SimpleInputFragment) mInputTable).setOnUpdateDataListener(new SimpleInputFragment.OnUpdateDataListener() {
            @Override
            public void onUpdate(ArrayList<SimpleInputRow> lists) {
                if (checkValue(simpleInputRows))
                    mChartView.updateData(simpleInputRows);
                else
                    Snackbar.make((View) mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
            }
        });
        mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
    }

    private void showLineChart() {
        advancedInputRows = new ArrayList<>();
        TypedArray colors = getResources().obtainTypedArray(R.array.mdcolor_500);
        int color = getResources().getColor(R.color.blue_500);
        ArrayList<String> values = new ArrayList<>();
        for (int j = 0; j < numOfCols; j++)
            values.add("Column"+j);
        advancedInputRows.add(new AdvancedInputRow("Field name", color, values, ""));
        for (int i = 0; i < numOfRows; i++) {
            color = getResources().getColor(R.color.blue_500);
            values = new ArrayList<>();
            for (int j = 0; j < numOfCols; j++)
                values.add("");
            advancedInputRows.add(new AdvancedInputRow("Tỉ lệ" + i, colors.getColor(i, color), values, "tỉ trọng " + i));
        }
        colors.recycle();
        mInputTable = new AdvancedInputFragment(advancedInputRows, numOfCols);
        layout.removeAllViews();
        mChartView = new LineChart(this, null);
        layout.addView((View) mChartView);
        ((AdvancedInputFragment) mInputTable).setOnUpdateDataListener(new AdvancedInputFragment.OnUpdateDataListener() {
            @Override
            public void onUpdate(ArrayList<AdvancedInputRow> lists) {
                //if (checkValue(advancedInputRows))
                mChartView.updateData(advancedInputRows);
                //else
                //Snackbar.make((View) mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
            }
        });
        mInputTable.show(getSupportFragmentManager(), AdvancedInputFragment.TAG);
    }

    /*private int getRandomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }*/

    private boolean checkValue(ArrayList<SimpleInputRow> list) {
        float sum = 0;
        for (SimpleInputRow row : list) {
            sum += Float.parseFloat(0 + row.getValue());
            if (row.getValue() == null || row.getValue().equals("")) {
                return false;
            }
        }
        return sum != 0;
    }

    /*private void createLegendTable(ArrayList<SimpleInputRow> list) {
        LegendTableRowAdapter rowsAdapter = new LegendTableRowAdapter(this, R.layout.legend_table_row, list);
        legendTableListView.setAdapter(rowsAdapter);
    }*/

    private int[] convertStringToIntArray(String str) {
        String[] tmp = str.split(" ");
        int[] result = new int[tmp.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(tmp[i]);
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit_chart:
                showBottomSheetDialog();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomSheetDialog() {
        if (mInputTable instanceof AdvancedInputFragment)
            mInputTable.show(getSupportFragmentManager(), AdvancedInputFragment.TAG);
        else mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
    }

    public ArrayList<SimpleInputRow> getSimpleInputRows() {
        return simpleInputRows;
    }

    public ArrayList<AdvancedInputRow> getAdvancedInputRows() {
        return advancedInputRows;
    }
}
