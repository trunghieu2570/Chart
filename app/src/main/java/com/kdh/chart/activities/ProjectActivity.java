package com.kdh.chart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
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
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.adapters.ChartListViewAdapter;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.fragments.CreateLineChartDialogFragment;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;
import com.kdh.chart.fragments.CreateProjectDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectActivity extends AppCompatActivity {

    public static final String PROJECT_LOCATION = "project";
    public static final String CHART = "chart";
    public static final String LOCATION = "location";
    public static final String BUNDLE = "bundle";
    private BottomSheetDialog bottomSheetDialog;
    private ListView chartListView;
    private ProjectLocation projectLocation;
    private ArrayList<Pair<ChartLocation, Chart>> charts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        final Bundle bundle = getIntent().getBundleExtra(CreateProjectDialogFragment.BUNDLE);
        projectLocation = (ProjectLocation) bundle.getSerializable(CreateProjectDialogFragment.PROJECT_LOCATION);
        final Project project = projectLocation.getProject();
        final String projectName = project.getName();
        setTitle(projectName);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottonSheetDialog(projectLocation);
            }
        });

        chartListView = findViewById(R.id.listview_chart);
        chartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Bundle bundle = new Bundle();
                final ChartLocation chartLocation = charts.get(i).first;
                final Chart chart = charts.get(i).second;
                switch (chartLocation.getType()) {
                    case PIE:
                        final Intent pieIntent = new Intent(ProjectActivity.this, PieChartActivity.class);
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        pieIntent.putExtra(BUNDLE, bundle);
                        startActivity(pieIntent);
                        break;
                    case LINE:
                        final Intent lineIntent = new Intent(ProjectActivity.this, LineChartActivity.class);
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        lineIntent.putExtra(BUNDLE, bundle);
                        startActivity(lineIntent);
                        break;
                }
            }
        });
        loadChartList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChartList();
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

    private void loadChartList() {
        //load chart list
        charts = ProjectFileManager.loadCharts(projectLocation);
        //map chart list
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (Pair<ChartLocation, Chart> chartPair : charts) {
            Map<String, String> tmp = new HashMap<>(2);
            tmp.put("Line1", chartPair.second.getChartName());
            tmp.put("Line2", chartPair.second.getDescription());
            data.add(tmp);
        }
        //add to listview
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"Line1", "Line2"}, new int[]{android.R.id.text1, android.R.id.text2});
        chartListView.setAdapter(simpleAdapter);
    }


    private void showBottonSheetDialog(final ProjectLocation projectLocation) {
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
                        final CreatePieChartDialogFragment fragment = CreatePieChartDialogFragment.newInstance(projectLocation);
                        fragment.show(getSupportFragmentManager(), "create_chart");
                        cancelBottonSheetDialog();
                        break;
                    case 1:
                        final CreateLineChartDialogFragment fragment2 = CreateLineChartDialogFragment.newInstance(projectLocation);
                        fragment2.show(getSupportFragmentManager(), "create_chart");
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

    public class ChartTypeItem {

        private int imageResouce;
        private String nameChart;

        public ChartTypeItem(int imageResouce, String nameChart) {
            this.imageResouce = imageResouce;
            this.nameChart = nameChart;
        }

        public void setimageResouce(int imageResouce) {
            this.imageResouce = imageResouce;
        }

        public void setNameChart(String nameChart) {
            this.nameChart = nameChart;
        }

        public int getimageResouce() {
            return imageResouce;
        }

        public String getNameChart() {
            return nameChart;
        }


    }
}
