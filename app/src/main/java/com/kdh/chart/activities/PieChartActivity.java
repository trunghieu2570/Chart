package com.kdh.chart.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.core.content.FileProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.BuildConfig;
import com.kdh.chart.FileManager;
import com.kdh.chart.R;
import com.kdh.chart.charts.PieChartView;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;
import com.kdh.chart.fragments.SimpleInputFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.kdh.chart.activities.StatisticPieChart.BUNDLE;
import static com.kdh.chart.activities.StatisticPieChart.CHART;

public class PieChartActivity extends AppCompatActivity implements ChartActivityInterface<SimpleInputRow>, SimpleInputFragment.OnUpdateDataListener {

    private PieChartView mChartView;
    private SimpleInputFragment mInputTable;
    private ArrayList<SimpleInputRow> simpleInputRows;
    private PieChart pieChart;
    private ProjectLocation projectLocation;
    private ChartLocation chartLocation;
    private Project project;
    private Vibrator vibrator;
    private LinearLayout chartLayout;


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
        chartLayout = findViewById(R.id.layout);
        chartLayout.removeAllViews();
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
        chartLayout.addView(mChartView);
        chartLayout.addView(chartTitle);
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
            case R.id.save_chart_as_picture:
                saveChartAsPicture();
                return true;
            case R.id.share_chart:
                shareChart();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveChartAsPicture() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", getResources().getConfiguration().locale);
        String imageName = dateFormat.format(Calendar.getInstance().getTime());
        File file = FileManager.saveImage(chartLayout, mInputTable.rowsListView, imageName);

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
        File file = FileManager.saveTempImage(chartLayout, mInputTable.rowsListView);
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
        if(!checkValue(simpleInputRows))
        {
            Snackbar.make(mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
            return;
        }
        final Bundle chartBundle = new Bundle();
        chartBundle.putSerializable(CHART, pieChart);
        //chartBundle.putSerializable(CHART_TYPE, ChartTypeEnum.PIE);
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
            FileManager.saveChart(projectLocation, pieChart, chartLocation);
            FileManager.saveProject(projectLocation);
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
            FileManager.saveProject(projectLocation);
            Log.d("Delete", "Delete successfully" + cList.size());
            Toast.makeText(this, "Xóa biểu đồ thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
