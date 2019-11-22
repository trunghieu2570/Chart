package com.kdh.chart.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.kdh.chart.R;
import com.kdh.chart.adapters.ChartListViewAdapter;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.ChartTypeItem;
import com.kdh.chart.fragments.CreateChartDialogFragment;
import com.kdh.chart.fragments.CreateProjectDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectActivity extends AppCompatActivity {

    private BottomSheetDialog bottomSheetDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle projectBundle = getIntent().getBundleExtra(CreateProjectDialogFragment.BUNDLE);
        String projectName = projectBundle.getString(CreateProjectDialogFragment.PROJECT_NAME);
        setTitle(projectName);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonSheetDialog();
            }
        });

        final ListView chartListView = findViewById(R.id.listview_chart);
        //project list
        ArrayList<Chart> charts = new ArrayList<>();
        //this loop will be removed soon
        for (int i = 0; i < 4; i++) {
            charts.add(new Chart(
                    "Chart " + i,
                    "This is a fake chart",
                    Calendar.getInstance().getTime().toString(),
                    Chart.ChartType.PIE
            ));
        }
        //map project list
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (Chart chart : charts) {
            Map<String, String> tmp = new HashMap<>(2);
            tmp.put("Line1", chart.getChartName());
            tmp.put("Line2", chart.getDescription());
            data.add(tmp);
        }
        //gan list project vao listview
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"Line1", "Line2"}, new int[]{android.R.id.text1, android.R.id.text2});
        chartListView.setAdapter(simpleAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void showBottonSheetDialog() {
        View dlview = getLayoutInflater().inflate(R.layout.fragment_main_bottom_sheet, null);
        //choose chart

        ArrayList<ChartTypeItem> arrayList = new ArrayList<>();

        ChartTypeItem typeChart1 = new ChartTypeItem(R.drawable.pie, "Pie");
        ChartTypeItem typeChart2 = new ChartTypeItem(R.drawable.line, "Line");
        ChartTypeItem typeChart3 = new ChartTypeItem(R.drawable.gr_chart, "Grouped");
        ChartTypeItem typeChart4 = new ChartTypeItem(R.drawable.donut, "Donut");
        ChartTypeItem typeChart5 = new ChartTypeItem(R.drawable.column, "Column");

        arrayList.add(typeChart1);
        arrayList.add(typeChart2);
        arrayList.add(typeChart3);
        arrayList.add(typeChart4);
        arrayList.add(typeChart5);
        GridView chooseChartListView = dlview.findViewById(R.id.listview_choose_chart);
        ChartListViewAdapter customAdapter = new ChartListViewAdapter(this, R.layout.list_view_item_chart_type, arrayList);
        chooseChartListView.setAdapter(customAdapter);


        chooseChartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Chart chart = new Chart(Chart.ChartType.PIE);
                        CreateChartDialogFragment fragment = CreateChartDialogFragment.newInstance(chart);
                        fragment.show(getSupportFragmentManager(), "create_chart");
                        cancelBottonSheetDialog();
                        break;
                }
            }
        });
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(dlview);
        bottomSheetDialog.show();
    }

    private void cancelBottonSheetDialog() {
        if (bottomSheetDialog != null)
            bottomSheetDialog.cancel();
    }
}
