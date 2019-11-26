package com.kdh.chart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.kdh.chart.ProjectFileManager;
import com.kdh.chart.R;
import com.kdh.chart.datatypes.ProjectLocation;
import com.kdh.chart.fragments.CreateProjectDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String BUNDLE = "bundle";
    public static final String PROJECT_LOCATION = "project";
    private ListView recentListView;
    private ArrayList<ProjectLocation> projectLocations;

    private void showCreateChartDialog() {
        CreateProjectDialogFragment createProjectDialogFragment = CreateProjectDialogFragment.newInstance();
        createProjectDialogFragment.show(getSupportFragmentManager(), "create_dialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setup
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        //floating button
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateChartDialog();
            }
        });

        recentListView = findViewById(R.id.listview_recent);
        recentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PROJECT_LOCATION, projectLocations.get(i));
                intent.putExtra(BUNDLE, bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProject();
    }

    private void loadProject() {
        //project list
        projectLocations = ProjectFileManager.loadProjects();
        if (projectLocations == null) return;
        //map project list
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (ProjectLocation projectLocation : projectLocations) {
            Map<String, String> tmp = new HashMap<>(2);
            tmp.put("Line1", projectLocation.getProject().getName());
            tmp.put("Line2", projectLocation.getProject().getModifiedTime());
            data.add(tmp);
        }
        //gan list project vao listview
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"Line1", "Line2"}, new int[]{android.R.id.text1, android.R.id.text2});
        recentListView.setAdapter(simpleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
