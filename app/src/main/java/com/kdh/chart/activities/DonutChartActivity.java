package com.kdh.chart.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.charts.DonutChartView;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.DonutChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;
import com.kdh.chart.fragments.SimpleInputFragment;

import org.greenrobot.eventbus.util.ErrorDialogManager;

import java.util.ArrayList;
import java.util.Calendar;

public class DonutChartActivity extends AppCompatActivity implements ChartActivityInterface<SimpleInputRow>, SimpleInputFragment.OnUpdateDataListener {

    private DonutChartView mChartView;
    private SimpleInputFragment mInputTable;
    private ArrayList<SimpleInputRow> simpleInputRows;
    private DonutChart donutChart;
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
            donutChart = (DonutChart) bundle.getSerializable(CreatePieChartDialogFragment.CHART);
            chartLocation = (ChartLocation) bundle.getSerializable(CreatePieChartDialogFragment.LOCATION);
        }
        //String chartName = donutChart.getChartName();
        project = projectLocation.getProject();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //prepare input table
        simpleInputRows = donutChart.getData();
        mInputTable = SimpleInputFragment.newInstance(simpleInputRows);
        //create chart
        LinearLayout layout = findViewById(R.id.layout);
        layout.removeAllViews();
        mChartView = new DonutChartView(this, null);
        final TextView chartTitle = new TextView(this);
        chartTitle.setText(donutChart.getChartName());
        chartTitle.setGravity(Gravity.CENTER);
        chartTitle.setPadding(10,0,10,0);
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
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBottomSheetDialog() {
        mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
    }

    /*public ArrayList<SimpleInputRow> getInputRows() {
        return simpleInputRows;
    }*/

    @Override
    public void onUpdate(ArrayList<SimpleInputRow> lists) {
        if (checkValue(simpleInputRows)) {
            //update data
            donutChart.setData(simpleInputRows);
            mChartView.updateData(simpleInputRows);
            //save data to file
            project.setModifiedTime(Calendar.getInstance().getTime().toString());
            ProjectFileManager.saveChart(projectLocation, donutChart, chartLocation);
            ProjectFileManager.saveProject(projectLocation);
        } else
            Snackbar.make(mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
    }

    private void deleteChart() {
        final ArrayList<ChartLocation> cList = projectLocation.getProject().getCharts();
        int target = -1;
        for(int i = 0; i < cList.size(); i++) {
            if (cList.get(i).getLocation().equals(chartLocation.getLocation())) {
                target = i;
            }
        }
        if(cList.remove(target) != null) {
            ProjectFileManager.saveProject(projectLocation);
            Log.d("Delete", "Delete successfully" + cList.size());
            Toast.makeText(this, "Xóa biểu đồ thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public ArrayList<SimpleInputRow> getInputRows() {
        return simpleInputRows;
    }
}
