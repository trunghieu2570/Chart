package com.kdh.chart.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.charts.PieChartView;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;
import com.kdh.chart.fragments.SimpleInputFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class PieChartActivity extends AppCompatActivity  implements SimpleInputFragment.OnUpdateDataListener {

    private PieChartView mChartView;
    private SimpleInputFragment mInputTable;
    private ArrayList<SimpleInputRow> simpleInputRows;
    private LinearLayout layout;
    private PieChart pieChart;
    private ProjectLocation projectLocation;
    private ChartLocation chartLocation;
    private Project project;
    private Bundle bundle;
    private String chartName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //receive bundle
        bundle = getIntent().getBundleExtra(CreatePieChartDialogFragment.BUNDLE);
        projectLocation = (ProjectLocation) bundle.getSerializable(CreatePieChartDialogFragment.PROJECT_LOCATION);
        pieChart = (PieChart) bundle.getSerializable(CreatePieChartDialogFragment.CHART);
        chartLocation = (ChartLocation) bundle.getSerializable(CreatePieChartDialogFragment.LOCATION);
        chartName = pieChart.getChartName();
        project = projectLocation.getProject();
        //set title
        if (chartName != null)
            setTitle(chartName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //prepare input table
        simpleInputRows = pieChart.getData();
        mInputTable = SimpleInputFragment.newInstance(simpleInputRows);
        //create chart
        layout = findViewById(R.id.layout);
        layout.removeAllViews();
        mChartView = new PieChartView(this, null);
        layout.addView((View) mChartView);
        // on input table data changed
/*        mInputTable.setOnUpdateDataListener(new SimpleInputFragment.OnUpdateDataListener() {
            @Override
            public void onUpdate(ArrayList<SimpleInputRow> lists) {

            }
        });*/
        //show input table
        if (simpleInputRows != null && checkValue(simpleInputRows))
            mChartView.updateData(simpleInputRows);
        else
            mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
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
        mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
    }

    public ArrayList<SimpleInputRow> getInputRows() {
        return simpleInputRows;
    }

    @Override
    public void onUpdate(ArrayList<SimpleInputRow> lists) {
        if (checkValue(simpleInputRows)) {
            //update data
            pieChart.setData(simpleInputRows);
            mChartView.updateData(simpleInputRows);
            //save data to file
            project.setModifiedTime(Calendar.getInstance().getTime().toString());
            pieChart.setModifiedTime(Calendar.getInstance().getTime().toString());
            ProjectFileManager.saveChart(projectLocation, pieChart, chartLocation);
            ProjectFileManager.saveProject(projectLocation);
        } else
            Snackbar.make(mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
    }
}
