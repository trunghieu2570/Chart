package com.kdh.chart.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.charts.ColumnBarChartView;
import com.kdh.chart.charts.GroupBarChartView;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ColumnBarChart;
import com.kdh.chart.datatypes.GroupBarChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.fragments.AdvancedInputFragment;
import com.kdh.chart.fragments.CreateColumnBarChartDialogFragment;
import com.kdh.chart.fragments.CreateGroupBarChartDialogFragment;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class ColumnBarChartActivity extends AppCompatActivity implements ChartActivityInterface<AdvancedInputRow>, AdvancedInputFragment.OnUpdateDataListener {

    private ColumnBarChartView mChartView;
    private DialogFragment mInputTable;
    private ArrayList<AdvancedInputRow> advancedInputRows;
    private ProjectLocation projectLocation;
    private ColumnBarChart barChart;
    private ChartLocation chartLocation;
    private Project project;
    private String chartName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        //receive bundle
        Bundle bundle = getIntent().getBundleExtra(CreateColumnBarChartDialogFragment.BUNDLE);
        if (bundle != null) {
            projectLocation = (ProjectLocation) bundle.getSerializable(CreateColumnBarChartDialogFragment.PROJECT_LOCATION);
            barChart = (ColumnBarChart) bundle.getSerializable(CreateColumnBarChartDialogFragment.CHART);
            chartLocation = (ChartLocation) bundle.getSerializable(CreateColumnBarChartDialogFragment.LOCATION);
        }
        chartName = barChart.getChartName();
        project = projectLocation.getProject();

        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        //prepare input table

        advancedInputRows = barChart.getData();
        mInputTable = AdvancedInputFragment.newInstance(advancedInputRows);
        //create chart
        LinearLayout layout = findViewById(R.id.layout);
        layout.removeAllViews();
        mChartView = new ColumnBarChartView(this, null);
        layout.addView(mChartView);
        //show input table
        if (checkValue(advancedInputRows)) {
            mChartView.updateData(advancedInputRows, chartName);
        } else
            mInputTable.show(getSupportFragmentManager(), AdvancedInputFragment.TAG);
    }

    private boolean checkValue(ArrayList<AdvancedInputRow> list) {
        for (AdvancedInputRow row : list.subList(1, list.size())) {
            for (String value : row.getValues()) {
                System.out.println("giá trị là: "+value);
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

    @Override
    public ArrayList<AdvancedInputRow> getInputRows() {
        return advancedInputRows;
    }

    @Override
    public void onUpdate(ArrayList<AdvancedInputRow> lists) {
        if (checkValue(advancedInputRows)) {
            //update data
            barChart.setData(advancedInputRows);
            mChartView.updateData(advancedInputRows, chartName);
            //save data to file
            project.setModifiedTime(Calendar.getInstance().getTime().toString());
            ProjectFileManager.saveChart(projectLocation, barChart, chartLocation);
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
}
