package com.kdh.chart.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.charts.LineChartView;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.LineChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.fragments.AdvancedInputFragment;
import com.kdh.chart.fragments.CreateLineChartDialogFragment;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class LineChartActivity extends AppCompatActivity {

    private LineChartView mChartView;
    private DialogFragment mInputTable;
    private ArrayList<AdvancedInputRow> advancedInputRows;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //receive bundle
        final Bundle bundle = getIntent().getBundleExtra(CreatePieChartDialogFragment.BUNDLE);
        final ProjectLocation projectLocation = (ProjectLocation) bundle.getSerializable(CreatePieChartDialogFragment.PROJECT_LOCATION);
        final LineChart lineChart = (LineChart) bundle.getSerializable(CreatePieChartDialogFragment.CHART);
        final ChartLocation location = (ChartLocation) bundle.getSerializable(CreatePieChartDialogFragment.LOCATION);
        final String chartName = lineChart.getChartName();
        final Project project = projectLocation.getProject();
        final int numOfRows = bundle.getInt(CreatePieChartDialogFragment.NUM_ROWS);
        final int numOfCols = bundle.getInt(CreateLineChartDialogFragment.NUM_COLS);
        final String xAxisUnit = lineChart.getxAxisUnit();
        final String yAxisUnit = lineChart.getxAxisUnit();
        //set title
        if (chartName != null)
            setTitle(chartName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //prepare input table

        advancedInputRows = new ArrayList<>();
        TypedArray colors = getResources().obtainTypedArray(R.array.mdcolor_500);
        int color = getResources().getColor(R.color.blue_500);
        //header
        ArrayList<String> values = new ArrayList<>();
        for (int j = 0; j < numOfCols; j++)
            values.add("C" + j);
        advancedInputRows.add(new AdvancedInputRow("Data table", color, values, ""));
        //content
        for (int i = 0; i < numOfRows; i++) {
            color = getResources().getColor(R.color.blue_500);
            values = new ArrayList<>();
            for (int j = 0; j < numOfCols; j++)
                values.add("");
            advancedInputRows.add(new AdvancedInputRow("R" + i, colors.getColor(i, color), values, "Description " + i));
        }
        colors.recycle();
        mInputTable = new AdvancedInputFragment(advancedInputRows, numOfCols);
        //create chart
        layout = findViewById(R.id.layout);
        layout.removeAllViews();
        mChartView = new LineChartView(this, null);
        layout.addView((View) mChartView);
        // on input table data changed
        ((AdvancedInputFragment) mInputTable).setOnUpdateDataListener(new AdvancedInputFragment.OnUpdateDataListener() {
            @Override
            public void onUpdate(ArrayList<AdvancedInputRow> lists) {
                if (checkValue(advancedInputRows)) {
                    //update data
                    lineChart.setData(advancedInputRows);
                    mChartView.updateData(advancedInputRows, chartName, xAxisUnit, yAxisUnit);
                    //save data to file
                    project.setModifiedTime(Calendar.getInstance().getTime().toString());
                    lineChart.setModifiedTime(Calendar.getInstance().getTime().toString());
                    ProjectFileManager.saveChart(projectLocation, lineChart, location);
                    ProjectFileManager.saveProject(projectLocation);
                } else
                    Snackbar.make((View) mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
            }
        });
        //show input table
        mInputTable.show(getSupportFragmentManager(), AdvancedInputFragment.TAG);
    }

    private boolean checkValue(ArrayList<AdvancedInputRow> list) {
        for (AdvancedInputRow row : list.subList(1, list.size())) {
            for (String value : row.getValues()) {
                if (value.equals(""))
                    return false;
            }
        }
        return true;
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
        mInputTable.show(getSupportFragmentManager(), AdvancedInputFragment.TAG);
    }

    public ArrayList<AdvancedInputRow> getInputRows() {
        return advancedInputRows;
    }
}
