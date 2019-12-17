package com.kdh.chart.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.chip.Chip;
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

import java.text.SimpleDateFormat;
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
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        final Chip mChip = findViewById(R.id.chip);
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
        mChartView.setOnPieItemSelectedListener(new PieChartView.OnPieItemSelectedListener() {
            @Override
            public void onSelected(int itemId) {
                vibrate();
                final ArrayList<SimpleInputRow> rows = pieChart.getData();
                Log.d("Debug", String.format("item %d", itemId));
                final String objectName = rows.get(itemId + 1).getLabel();
                final String value = rows.get(itemId + 1).getValue();
                //final String valueGroupName = rows.get(0).getValue();
                //final String groupName = rows.get(0).getLabel();
                mChip.setVisibility(View.VISIBLE);
                mChip.setText(String.format("Giá trị của %s là %s", objectName, value));
            }

            @Override
            public void onUnselected() {
                vibrate();
                mChip.setVisibility(View.GONE);
            }
        });
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
        for (SimpleInputRow row : list.subList(1, list.size())) {
            if (row.getValue() == null || row.getValue().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(50);
        }
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
        Intent describeIntent = new Intent(PieChartActivity.this, StatisticPieChart.class);
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", getResources().getConfiguration().locale);
            project.setModifiedTime(dateFormat.format(Calendar.getInstance().getTime()));
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
