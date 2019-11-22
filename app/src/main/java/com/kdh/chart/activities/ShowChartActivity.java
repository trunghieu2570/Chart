package com.kdh.chart.activities;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.R;
import com.kdh.chart.adapters.LegendTableRowAdapter;
import com.kdh.chart.charts.ChartView;
import com.kdh.chart.charts.PieChart;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.CreateChartDialogFragment;
import com.kdh.chart.fragments.SimpleInputFragment;

import java.util.ArrayList;
import java.util.Random;

public class ShowChartActivity extends AppCompatActivity {

    private ChartView mChartView;
    private SimpleInputFragment mInputTable;
    private ArrayList<SimpleInputRow> simpleInputRows;
    private ListView legendTableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        final Bundle bundle = getIntent().getBundleExtra(CreateChartDialogFragment.BUNDLE);
        final String chartName = bundle.getString(CreateChartDialogFragment.CHART_NAME);
        final int numOfRows = bundle.getInt(CreateChartDialogFragment.NUM_ROWS);
        final Chart chartType = (Chart) bundle.getSerializable(CreateChartDialogFragment.CHART_TYPE);
        if (chartName != null)
            setTitle(chartName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        simpleInputRows = new ArrayList<>();
        TypedArray colors = getResources().obtainTypedArray(R.array.mdcolor_500);
        for (int i = 0; i < numOfRows; i++) {
            int color = getResources().getColor(R.color.blue_500);
            simpleInputRows.add(new SimpleInputRow("Tỉ lệ" + i, colors.getColor(i, color), "", "tỉ trọng " + i));
        }
        colors.recycle();
        mInputTable = new SimpleInputFragment(simpleInputRows);

        final LinearLayout layout = findViewById(R.id.layout);
        switch (chartType.getType()) {
            case PIE:
                mChartView = new PieChart(this, null);
                break;
        }
        layout.removeAllViews();
        layout.addView((View) mChartView);
        mInputTable.setOnUpdateDataListener(new SimpleInputFragment.OnUpdateDataListener() {
            @Override
            public void onUpdate(ArrayList<SimpleInputRow> lists) {
                if (checkValue(simpleInputRows))
                    setValues(simpleInputRows);
                else
                    Snackbar.make((View) mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
            }
        });
        showBottomSheetDialog();
    }

    private int getRandomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    private void setValues(ArrayList<SimpleInputRow> list) {
        mChartView.updateData(list);
        //createLegendTable(list);
    }

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

    private void createLegendTable(ArrayList<SimpleInputRow> list) {
        LegendTableRowAdapter rowsAdapter = new LegendTableRowAdapter(this, R.layout.legend_table_row, list);
        legendTableListView.setAdapter(rowsAdapter);
    }

    private int[] convertStringToIntArray(String str) {
        String[] tmp = str.split(" ");
        int[] result = new int[tmp.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(tmp[i]);
        }
        return result;
    }

    private void showBottomSheetDialog() {
        mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
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

    public ArrayList<SimpleInputRow> getInputRows() {
        return simpleInputRows;
    }
}
