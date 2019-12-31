package com.kdh.chart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kdh.chart.FileManager;
import com.kdh.chart.R;
import com.kdh.chart.adapters.ChartListViewAdapter;
import com.kdh.chart.datatypes.Chart;
import com.kdh.chart.datatypes.ChartLocation;
import com.kdh.chart.datatypes.Project;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.fragments.CreateColumnBarChartDialogFragment;
import com.kdh.chart.fragments.CreateDonutChartDialogFragment;
import com.kdh.chart.fragments.CreateGroupBarChartDialogFragment;
import com.kdh.chart.fragments.CreateLineChartDialogFragment;
import com.kdh.chart.fragments.CreatePieChartDialogFragment;
import com.kdh.chart.fragments.CreateProjectDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProjectActivity extends AppCompatActivity {

    public static final String PROJECT_LOCATION = "project";
    public static final String CHART = "chart";
    public static final String LOCATION = "location";
    public static final String BUNDLE = "bundle";
    private BottomSheetDialog bottomSheetDialog;
    private ListView chartListView;
    private ProjectLocation projectLocation;
    private View emptyView;
    private ArrayList<Pair<ChartLocation, Chart>> charts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.chart_list);
        final ImageButton btGoHome = findViewById(R.id.bt_go_home);
        btGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final TextView tvProjectName = findViewById(R.id.tv_project_name);
        emptyView = findViewById(R.id.project_empty);
        final Bundle bundle = getIntent().getBundleExtra(CreateProjectDialogFragment.BUNDLE);
        if (bundle != null) {
            projectLocation = (ProjectLocation) bundle.getSerializable(CreateProjectDialogFragment.PROJECT_LOCATION);
            final Project project;
            if (projectLocation != null) {
                project = projectLocation.getProject();
                Log.d("create", "create" + project.getCharts().size());
                final String projectName = project.getName();
                tvProjectName.setText(projectName);
            }
        }
        chartListView = findViewById(R.id.listview_chart);
        loadChartList();
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog(projectLocation);
            }
        });


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
                    case DONUT:
                        final Intent donutIntent = new Intent(ProjectActivity.this, DonutChartActivity.class);
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        donutIntent.putExtra(BUNDLE, bundle);
                        startActivity(donutIntent);
                        break;
                    case GROUPED:
                        final Intent groupIntent = new Intent(ProjectActivity.this, GroupBarChartActivity.class);
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        groupIntent.putExtra(BUNDLE, bundle);
                        startActivity(groupIntent);
                        break;
                    case COLUMN:
                        final Intent columnIntent = new Intent(ProjectActivity.this, ColumnBarChartActivity.class);
                        bundle.putSerializable(PROJECT_LOCATION, projectLocation);
                        bundle.putSerializable(CHART, chart);
                        bundle.putSerializable(LOCATION, chartLocation);
                        columnIntent.putExtra(BUNDLE, bundle);
                        startActivity(columnIntent);
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChartList();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadChartList() {
        chartListView.setVisibility(GONE);
        emptyView.setVisibility(VISIBLE);
        //load chart list
        projectLocation = FileManager.loadProject(projectLocation);
        charts = FileManager.loadCharts(projectLocation);
        if (charts.size() > 0) {
            chartListView.setVisibility(VISIBLE);
            emptyView.setVisibility(GONE);
        }
        //map chart list
        List<Map<String, String>> data = new ArrayList<>();
        for (Pair<ChartLocation, Chart> chartPair : charts) {
            Map<String, String> tmp = new HashMap<>(2);
            tmp.put("Line1", chartPair.second.getChartName());
            tmp.put("Line2", chartPair.second.getDescription());
            data.add(tmp);
        }
        //add to listView
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"Line1", "Line2"}, new int[]{android.R.id.text1, android.R.id.text2});
        chartListView.setAdapter(simpleAdapter);
    }


    private void showBottomSheetDialog(final ProjectLocation projectLocation) {
        View dlView = getLayoutInflater().inflate(R.layout.fragment_main_bottom_sheet, null);
        //choose chart

        ArrayList<ChartTypeItem> arrayList = new ArrayList<>();

        ChartTypeItem typeChart1 = new ChartTypeItem(R.drawable.pie, "Tròn");
        ChartTypeItem typeChart2 = new ChartTypeItem(R.drawable.line, "Đường");
        ChartTypeItem typeChart3 = new ChartTypeItem(R.drawable.gr_chart, "Nhóm");
        ChartTypeItem typeChart4 = new ChartTypeItem(R.drawable.donut, "Donut");
        ChartTypeItem typeChart5 = new ChartTypeItem(R.drawable.column, "Cột");

        arrayList.add(typeChart1);
        arrayList.add(typeChart2);
        arrayList.add(typeChart3);
        arrayList.add(typeChart4);
        arrayList.add(typeChart5);
        GridView chooseChartListView = dlView.findViewById(R.id.listview_choose_chart);
        ChartListViewAdapter customAdapter = new ChartListViewAdapter(this, R.layout.list_view_item_chart_type, arrayList);
        chooseChartListView.setAdapter(customAdapter);


        chooseChartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        final CreatePieChartDialogFragment fragment = CreatePieChartDialogFragment.newInstance(projectLocation);
                        fragment.setOnChartNameDuplicatedListener(new CreatePieChartDialogFragment.OnChartNameDuplicatedListener() {
                            @Override
                            public void onDuplicated() {
                                Snackbar.make(chartListView, "Tên biểu đồ này đã được sử dụng", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        fragment.show(getSupportFragmentManager(), "create_chart");
                        cancelButtonSheetDialog();
                        break;
                    case 1:
                        final CreateLineChartDialogFragment fragment2 = CreateLineChartDialogFragment.newInstance(projectLocation);
                        fragment2.setOnChartNameDuplicatedListener(new CreateLineChartDialogFragment.OnChartNameDuplicatedListener() {
                            @Override
                            public void onDuplicated() {
                                Snackbar.make(chartListView, "Tên biểu đồ này đã được sử dụng", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        fragment2.show(getSupportFragmentManager(), "create_chart");
                        cancelButtonSheetDialog();
                        break;
                    case 2:
                        final CreateGroupBarChartDialogFragment fragment5 = CreateGroupBarChartDialogFragment.newInstance(projectLocation);
                        fragment5.setOnChartNameDuplicatedListener(new CreateGroupBarChartDialogFragment.OnChartNameDuplicatedListener() {
                            @Override
                            public void onDuplicated() {
                                Snackbar.make(chartListView, "Tên biểu đồ này đã được sử dụng", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        fragment5.show(getSupportFragmentManager(), "create_chart");
                        cancelButtonSheetDialog();
                        break;
                    case 3:
                        final CreateDonutChartDialogFragment fragment3 = CreateDonutChartDialogFragment.newInstance(projectLocation);
                        fragment3.setOnChartNameDuplicatedListener(new CreateDonutChartDialogFragment.OnChartNameDuplicatedListener() {
                            @Override
                            public void onDuplicated() {
                                Snackbar.make(chartListView, "Tên biểu đồ này đã được sử dụng", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        fragment3.show(getSupportFragmentManager(), "create_chart");
                        cancelButtonSheetDialog();
                        break;
                    case 4:
                        final CreateColumnBarChartDialogFragment fragment6 = CreateColumnBarChartDialogFragment.newInstance(projectLocation);
                        fragment6.setOnChartNameDuplicatedListener(new CreateColumnBarChartDialogFragment.OnChartNameDuplicatedListener() {
                            @Override
                            public void onDuplicated() {
                                Snackbar.make(chartListView, "Tên biểu đồ này đã được sử dụng", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        fragment6.show(getSupportFragmentManager(), "creat_chart");
                        cancelButtonSheetDialog();
                        break;
                }
            }
        });
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(dlView);
        bottomSheetDialog.show();
    }

    private void cancelButtonSheetDialog() {
        if (bottomSheetDialog != null)
            bottomSheetDialog.cancel();
    }

    public class ChartTypeItem {

        private int imageResource;
        private String nameChart;

        ChartTypeItem(int imageResource, String nameChart) {
            this.imageResource = imageResource;
            this.nameChart = nameChart;
        }

        public int getImageResource() {
            return imageResource;
        }

        public String getNameChart() {
            return nameChart;
        }
    }
}
