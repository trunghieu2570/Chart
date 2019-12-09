package com.kdh.chart.activities;

import android.os.Bundle;
import android.util.Log;
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

public class LineChartActivity extends AppCompatActivity implements AdvancedInputFragment.OnUpdateDataListener {

    private LineChartView mChartView;
    private DialogFragment mInputTable;
    private ArrayList<AdvancedInputRow> advancedInputRows;
    private LinearLayout layout;
    private Bundle bundle;
    private ProjectLocation projectLocation;
    private LineChart lineChart;
    private ChartLocation chartLocation;
    private Project project;
    private String chartName;
    private String xAxisUnit;
    private String yAxisUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //receive bundle
        bundle = getIntent().getBundleExtra(CreatePieChartDialogFragment.BUNDLE);
        projectLocation = (ProjectLocation) bundle.getSerializable(CreateLineChartDialogFragment.PROJECT_LOCATION);
        lineChart = (LineChart) bundle.getSerializable(CreateLineChartDialogFragment.CHART);
        chartLocation = (ChartLocation) bundle.getSerializable(CreateLineChartDialogFragment.LOCATION);
        chartName = lineChart.getChartName();
        project = projectLocation.getProject();
        xAxisUnit = lineChart.getxAxisUnit();
        yAxisUnit = lineChart.getxAxisUnit();
        //set title
        if (chartName != null)
            setTitle(chartName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //prepare input table

        advancedInputRows = lineChart.getData();
        mInputTable = AdvancedInputFragment.newInstance(advancedInputRows);
        //create chart
        layout = findViewById(R.id.layout);
        layout.removeAllViews();
        mChartView = new LineChartView(this, null);
        layout.addView((View) mChartView);
        //show input table
        if (checkValue(advancedInputRows)) {
            mChartView.updateData(advancedInputRows, chartName, xAxisUnit, yAxisUnit);
        }
        else
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
            case R.id.delete_chart:
                deleteChart();
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

    @Override
    public void onUpdate(ArrayList<AdvancedInputRow> lists) {
        if (checkValue(advancedInputRows)) {
            //update data
            lineChart.setData(advancedInputRows);
            mChartView.updateData(advancedInputRows, chartName, xAxisUnit, yAxisUnit);
            //save data to file
            project.setModifiedTime(Calendar.getInstance().getTime().toString());
            lineChart.setModifiedTime(Calendar.getInstance().getTime().toString());
            ProjectFileManager.saveChart(projectLocation, lineChart, chartLocation);
            ProjectFileManager.saveProject(projectLocation);
        } else
            Snackbar.make((View) mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
    }

    private void deleteChart() {
        final ArrayList<ChartLocation> cList = projectLocation.getProject().getCharts();
        for(int i = 0; i < cList.size(); i++) {
            if (cList.get(i).getLocation().equals(chartLocation.getLocation())) {
                projectLocation.getProject().getCharts().remove(i);
                ProjectFileManager.saveProject(projectLocation);
                Log.d("Delete", "Delete successfully");
            }
        }
        finish();
    }
}
