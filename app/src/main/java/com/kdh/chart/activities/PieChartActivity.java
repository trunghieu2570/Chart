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
import com.kdh.chart.charts.PieChartView;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;
import com.kdh.chart.fragments.SimpleInputFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class PieChartActivity extends AppCompatActivity {

    private PieChartView mChartView;
    private DialogFragment mInputTable;
    private ArrayList<SimpleInputRow> simpleInputRows;

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
        final Project project = (Project) bundle.getSerializable(CreatePieChartDialogFragment.PROJECT);
        final PieChart pieChart = (PieChart) bundle.getSerializable(CreatePieChartDialogFragment.CHART);
        final ChartLocation location = (ChartLocation) bundle.getSerializable(CreatePieChartDialogFragment.LOCATION);
        final String chartName = pieChart.getChartName();
        final int numOfRows = bundle.getInt(CreatePieChartDialogFragment.NUM_ROWS);
        //set title
        if (chartName != null)
            setTitle(chartName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //prepare input table

        simpleInputRows = new ArrayList<>();
        TypedArray colors = getResources().obtainTypedArray(R.array.mdcolor_500);
        for (int i = 0; i < numOfRows; i++) {
            int color = getResources().getColor(R.color.blue_500);
            simpleInputRows.add(new SimpleInputRow("R " + i, colors.getColor(i, color), "", "Description " + i));
        }
        colors.recycle();
        mInputTable = new SimpleInputFragment(simpleInputRows);
        //create chart
        layout = findViewById(R.id.layout);
        layout.removeAllViews();
        mChartView = new PieChartView(this, null);
        layout.addView((View) mChartView);
        // on input table data changed
        ((SimpleInputFragment) mInputTable).setOnUpdateDataListener(new SimpleInputFragment.OnUpdateDataListener() {
            @Override
            public void onUpdate(ArrayList<SimpleInputRow> lists) {
                if (checkValue(simpleInputRows)){
                    //update data
                    pieChart.setData(simpleInputRows);
                    mChartView.updateData(simpleInputRows);
                    //save data to file
                    project.setModifiedTime(Calendar.getInstance().getTime().toString());
                    pieChart.setModifiedTime(Calendar.getInstance().getTime().toString());
                    ProjectFileManager.saveChart(project, pieChart, location);
                    ProjectFileManager.saveProject(project);
                }

                else
                    Snackbar.make((View) mChartView, "Invalid data", Snackbar.LENGTH_SHORT).show();
            }
        });
        //show input table
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

}
