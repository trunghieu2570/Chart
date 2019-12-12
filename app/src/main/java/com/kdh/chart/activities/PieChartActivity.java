package com.kdh.chart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.charts.PieChartView;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ChartTypeEnum;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;
import com.kdh.chart.fragments.SimpleInputFragment;

import java.util.ArrayList;
import java.util.Calendar;

import static com.kdh.chart.activities.ChartDescribeActivity.BUNDLE;
import static com.kdh.chart.activities.ChartDescribeActivity.CHART;
import static com.kdh.chart.activities.ChartDescribeActivity.CHART_TYPE;

public class PieChartActivity extends AppCompatActivity implements ChartActivityInterface<SimpleInputRow>, SimpleInputFragment.OnUpdateDataListener {

    private PieChartView mChartView;
    private SimpleInputFragment mInputTable;
    private ArrayList<SimpleInputRow> simpleInputRows;
    private PieChart pieChart;
    private ProjectLocation projectLocation;
    private ChartLocation chartLocation;
    private Project project;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        //receive bundle
        Bundle bundle = getIntent().getBundleExtra(CreatePieChartDialogFragment.BUNDLE);
        if (bundle != null) {
            projectLocation = (ProjectLocation) bundle.getSerializable(CreatePieChartDialogFragment.PROJECT_LOCATION);
            pieChart = (PieChart) bundle.getSerializable(CreatePieChartDialogFragment.CHART);
            chartLocation = (ChartLocation) bundle.getSerializable(CreatePieChartDialogFragment.LOCATION);
        }
        project = projectLocation.getProject();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //prepare input table
        simpleInputRows = pieChart.getData();
        mInputTable = SimpleInputFragment.newInstance(simpleInputRows);
        //create chart
        LinearLayout layout = findViewById(R.id.layout);
        layout.removeAllViews();
        mChartView = new PieChartView(this, null);
        final TextView chartTitle = new TextView(this);
        chartTitle.setText(pieChart.getChartName());
        chartTitle.setGravity(Gravity.CENTER);
        chartTitle.setPadding(10, 0, 10, 0);
        layout.addView(mChartView);
        layout.addView(chartTitle);
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
        for (SimpleInputRow row : list.subList(1, list.size())) {
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
            case R.id.delete_chart:
                deleteChart();
                return true;
            case R.id.describe_chart:
                describeChart();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Nhận xét
    private void describeChart() {
        final Bundle chartBundle = new Bundle();
        chartBundle.putSerializable(CHART, pieChart);
        chartBundle.putSerializable(CHART_TYPE, ChartTypeEnum.PIE);
        Intent describeIntent = new Intent(PieChartActivity.this, ChartDescribeActivity.class);
        describeIntent.putExtra(BUNDLE, chartBundle);
        startActivity(describeIntent);
    }

    private void showBottomSheetDialog() {
        mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
    }

    @Override
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
            ProjectFileManager.saveChart(projectLocation, pieChart, chartLocation);
            ProjectFileManager.saveProject(projectLocation);
        } else
            Snackbar.make(mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
    }

    private void deleteChart() {
        final ArrayList<ChartLocation> cList = projectLocation.getProject().getCharts();
        int target = -1;
        for (int i = 0; i < cList.size(); i++) {
            if (cList.get(i).getLocation().equals(chartLocation.getLocation())) {
                target = i;
            }
        }
        if (cList.remove(target) != null) {
            ProjectFileManager.saveProject(projectLocation);
            Log.d("Delete", "Delete successfully" + cList.size());
            Toast.makeText(this, "Xóa biểu đồ thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
