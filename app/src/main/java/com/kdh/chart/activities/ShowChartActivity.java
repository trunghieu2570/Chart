package com.kdh.chart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kdh.chart.R;
import com.kdh.chart.charts.PieChart;
import com.kdh.chart.fragments.InputBottomSheetFragment;

public class ShowChartActivity extends AppCompatActivity {

    private Button updateButton;
    private Button updateTogetherButton;
    private Button updateSequentiallyButton;
    private Button fixChart;
    private PieChart pieChart;
    private TextView tv_chartName;
    private String values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pie_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        setTitle("Your Pie Chart");
        actionBar.setDisplayHomeAsUpEnabled(true);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pieChart = findViewById(R.id.pieChart);

        values="1";

        SetInputValues();
        showBottomSheetDialog();
    }

    public void SetInputValues()
    {


        Intent intent=getIntent();
        Bundle bundle = intent.getBundleExtra(InputPie.BUNDLE);

        if (intent != null) {
            bundle = intent.getBundleExtra(InputPie.BUNDLE);
            if (bundle != null) {
                values=bundle.getString(InputPie.VALUE).toString();
                tv_chartName.setText("Pie Chart: "+bundle.getString(InputPie.CHART_NAME).toString());
                updateButton.callOnClick();
            }
        }
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
        InputBottomSheetFragment bottomSheetFragment = InputBottomSheetFragment.newInstance();
        bottomSheetFragment.show(getSupportFragmentManager(),InputBottomSheetFragment.TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_chart, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_chart) {
            showBottomSheetDialog();
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
