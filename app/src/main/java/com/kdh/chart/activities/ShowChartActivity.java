package com.kdh.chart.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kdh.chart.R;
import com.kdh.chart.adapters.LegendTableRowAdapter;
import com.kdh.chart.charts.ChartView;
import com.kdh.chart.datatypes.SimpleInputRow;
import com.kdh.chart.fragments.SimpleInputFragment;

import java.util.ArrayList;
import java.util.Random;

public class ShowChartActivity extends AppCompatActivity {

    private ChartView mChartView;
    private SimpleInputFragment mInputTable;
    private ArrayList<SimpleInputRow> simpleInputRows;
    private ListView legendTableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        setTitle("Your Pie Chart");
        actionBar.setDisplayHomeAsUpEnabled(true);
        simpleInputRows = new ArrayList<>();
        String[] fields = new String[]{"Tỉ trọng A", "Tỉ trọng B", "Tỉ trọng C"};
        for (String lb : fields) {
            simpleInputRows.add(new SimpleInputRow(lb, R.color.blue_500, "","tỉ trọng 1"));
        }
        mInputTable = new SimpleInputFragment(simpleInputRows);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mChartView = findViewById(R.id.pieChart);
        //legendTableListView = findViewById(R.id.lv_legend_table);

        //Gán sự kiện khi cập nhật data
        mInputTable.setOnUpdateDataListener(new SimpleInputFragment.OnUpdateDataListener() {
            @Override
            public void onUpdate(ArrayList<SimpleInputRow> lists) {
                SetValue(simpleInputRows);
            }
        });
        showBottomSheetDialog();
    }
    private int getRandomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    private void SetValue(ArrayList<SimpleInputRow> list)
    {
        mChartView.updateData(list);
        //createLegendTable(list);
    }

    private void createLegendTable(ArrayList<SimpleInputRow> list) {
        LegendTableRowAdapter rowsAdapter = new LegendTableRowAdapter(this, R.layout.legend_table_row, list);
        legendTableListView.setAdapter(rowsAdapter);
    }

    private int[] convertStringToIntArray(String str) {
            String[] tmp = str.split(" ");
            int[] result = new int[tmp.length];
            for(int i = 0; i < result.length; i++) {
                result[i] = Integer.parseInt(tmp[i]);
            }
            return result;
    }

    private void showBottomSheetDialog() {
        mInputTable.show(getSupportFragmentManager(), SimpleInputFragment.TAG);
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

    public ArrayList<SimpleInputRow> getInputRows() {
        return simpleInputRows;
    }
}
