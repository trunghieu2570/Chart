package com.kdh.chart.activities;

import android.content.Context;
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
import com.kdh.chart.charts.DonutChartView;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.DonutChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.fragments.AdvancedInputFragment;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;
import com.kdh.chart.fragments.SimpleInputFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class DonutChartActivity extends AppCompatActivity implements ChartActivityInterface<AdvancedInputRow>, AdvancedInputFragment.OnUpdateDataListener {

    private DonutChartView mChartView;
    private AdvancedInputFragment mInputTable;
    private ArrayList<AdvancedInputRow> advancedInputRows;
    private DonutChart donutChart;
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
            donutChart = (DonutChart) bundle.getSerializable(CreatePieChartDialogFragment.CHART);
            chartLocation = (ChartLocation) bundle.getSerializable(CreatePieChartDialogFragment.LOCATION);
        }
        //String chartName = donutChart.getChartName();
        project = projectLocation.getProject();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //prepare input table
        advancedInputRows = donutChart.getData();
        mInputTable = AdvancedInputFragment.newInstance(advancedInputRows);
        //create chart
        LinearLayout layout = findViewById(R.id.layout);
        layout.removeAllViews();
        mChartView = new DonutChartView(this, null);
        mChartView.setOnDonutItemSelectedListener(new DonutChartView.OnDonutItemSelectedListener() {
            @Override
            public void onSelected(int ringId, int itemId) {
                vibrate();
                final ArrayList<AdvancedInputRow> rows = donutChart.getData();
                Log.d("Debug", String.format("item %d at ring %d", itemId, ringId));
                final String objectName = rows.get(itemId + 1).getLabel();
                final String valueName = rows.get(0).getValues().get(ringId);
                final String value = rows.get(itemId + 1).getValues().get(ringId);
                final String seriesMeaning = donutChart.getSeriesMeaning();
                final String valuesMeaning = donutChart.getValuesMeaning();
                mChip.setVisibility(View.VISIBLE);
                mChip.setText(String.format("Giá trị của %s %s %s là %s", objectName, seriesMeaning.toLowerCase(), valueName, value));
            }

            @Override
            public void onUnselected() {
                vibrate();
                mChip.setVisibility(View.GONE);
            }
        });
        final TextView chartTitle = new TextView(this);
        chartTitle.setText(donutChart.getChartName());
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
        if (checkValue(advancedInputRows)) {
            final int size = advancedInputRows.get(0).getValues().size() + 2;
            mChartView.updateData(advancedInputRows);
            mChartView.setLayoutParams(new LinearLayout.LayoutParams(size * (int) (DonutChartView.DONUT_WIDTH) * 2, size * (int) DonutChartView.DONUT_WIDTH * 2));
        } else
            mInputTable.show(getSupportFragmentManager(), AdvancedInputFragment.TAG);
    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(50);
        }
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
        mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
    }

    /*public ArrayList<SimpleInputRow> getInputRows() {
        return simpleInputRows;
    }*/

    @Override
    public void onUpdate(ArrayList<AdvancedInputRow> lists) {
        if (checkValue(advancedInputRows)) {
            final int size = advancedInputRows.get(0).getValues().size() + 2;
            //update data
            donutChart.setData(advancedInputRows);
            mChartView.updateData(advancedInputRows);
            mChartView.setLayoutParams(new LinearLayout.LayoutParams(size * (int) (DonutChartView.DONUT_WIDTH) * 2, size * (int) DonutChartView.DONUT_WIDTH * 2));
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

    @Override
    public ArrayList<AdvancedInputRow> getInputRows() {
        return advancedInputRows;
    }
}
