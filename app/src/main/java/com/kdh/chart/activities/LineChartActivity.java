package com.kdh.chart.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.BuildConfig;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.charts.LineChartView;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.ChartTypeEnum;
import com.kdh.chart.datatypes.LineChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.fragments.AdvancedInputFragment;
import com.kdh.chart.fragments.CreateLineChartDialogFragment;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.kdh.chart.activities.ChartDescribeActivity.BUNDLE;
import static com.kdh.chart.activities.ChartDescribeActivity.CHART;
import static com.kdh.chart.activities.ChartDescribeActivity.CHART_TYPE;

public class LineChartActivity extends AppCompatActivity implements ChartActivityInterface<AdvancedInputRow>, AdvancedInputFragment.OnUpdateDataListener {

    private LineChartView mChartView;
    private AdvancedInputFragment mInputTable;
    private ArrayList<AdvancedInputRow> advancedInputRows;
    private ProjectLocation projectLocation;
    private LineChart lineChart;
    private ChartLocation chartLocation;
    private Project project;
    private String chartName;
    private String xAxisUnit;
    private String yAxisUnit;
    private LinearLayout chartLayout;

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
            projectLocation = (ProjectLocation) bundle.getSerializable(CreateLineChartDialogFragment.PROJECT_LOCATION);
            lineChart = (LineChart) bundle.getSerializable(CreateLineChartDialogFragment.CHART);
            chartLocation = (ChartLocation) bundle.getSerializable(CreateLineChartDialogFragment.LOCATION);
        }
        chartName = lineChart.getChartName();
        project = projectLocation.getProject();
        xAxisUnit = lineChart.getxAxisUnit();
        yAxisUnit = lineChart.getyAxisUnit();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        //prepare input table

        advancedInputRows = lineChart.getData();
        mInputTable = AdvancedInputFragment.newInstance(advancedInputRows);
        //create chart
        chartLayout = findViewById(R.id.layout);
        chartLayout.removeAllViews();
        mChartView = new LineChartView(this, null);
        chartLayout.addView(mChartView);
        //show input table
        if (checkValue(advancedInputRows)) {
            mChartView.updateData(advancedInputRows, chartName, xAxisUnit, yAxisUnit);
        } else
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
            case R.id.save_chart_as_picture:
                saveChartAsPicture();
                return true;
            case R.id.share_chart:
                shareChart();
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

    private void saveChartAsPicture() {
        File file = ProjectFileManager.saveImage(this, chartLayout, mInputTable.rowsListView);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(photoUri, "image/png");
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.fromFile(file);
            intent.setDataAndType(data, "image/png");
            startActivity(intent);
        }
    }

    private void shareChart() {
        File file = ProjectFileManager.saveImage(this, chartLayout, mInputTable.rowsListView);
        Uri uriToImage;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uriToImage = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } else {
            uriToImage = Uri.fromFile(file);
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_chart_menu)));
    }

    //Nhận xét
    private void describeChart() {
        if(!checkValue(advancedInputRows))
        {
            Snackbar.make(mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
            return;
        }
        final Bundle chartBundle = new Bundle();
        chartBundle.putSerializable(CHART, lineChart);
        chartBundle.putSerializable(CHART_TYPE, ChartTypeEnum.LINE);
        Intent describeIntent = new Intent(LineChartActivity.this, StatisticLineChart.class);
        describeIntent.putExtra(BUNDLE, chartBundle);
        startActivity(describeIntent);
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
            lineChart.setData(advancedInputRows);
            mChartView.updateData(advancedInputRows, chartName, xAxisUnit, yAxisUnit);
            //save data to file
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", getResources().getConfiguration().locale);
            project.setModifiedTime(dateFormat.format(Calendar.getInstance().getTime()));
            ProjectFileManager.saveChart(projectLocation, lineChart, chartLocation);
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
