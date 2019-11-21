package com.kdh.chart.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.kdh.chart.R;
import com.kdh.chart.fragments.CreateProjectDialogFragment;
import com.kdh.chart.datatypes.ChartTypeItem;
import com.kdh.chart.datatypes.Project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {




    private void showCreateChartDialog() {
        CreateProjectDialogFragment createProjectDialogFragment = new CreateProjectDialogFragment(this, new ChartTypeItem(0, "Pie"));
        createProjectDialogFragment.show(getSupportFragmentManager(), "create_dialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setup
        setSupportActionBar(toolbar);

        //floating button
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateChartDialog();
            }
        });


        final ListView recentListView = findViewById(R.id.listview_recent);
        //project list
        ArrayList<Project> projects = new ArrayList<>();
        projects.add(new Project("Project 1", Calendar.getInstance().getTime().toString()));
        projects.add(new Project("Project 2", Calendar.getInstance().getTime().toString()));
        projects.add(new Project("Project 3", Calendar.getInstance().getTime().toString()));
        projects.add(new Project("Project 4", Calendar.getInstance().getTime().toString()));
        projects.add(new Project("Project 5", Calendar.getInstance().getTime().toString()));
        //map project list
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (Project project : projects) {
            Map<String, String> tmp = new HashMap<>(2);
            tmp.put("Line1", project.getProjectName());
            tmp.put("Line2", project.getModifiedTime());
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
