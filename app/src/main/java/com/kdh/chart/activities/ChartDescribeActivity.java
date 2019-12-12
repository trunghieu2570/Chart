package com.kdh.chart.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kdh.chart.R;
import com.kdh.chart.datatypes.ChartTypeEnum;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.SimpleInputRow;

import java.util.ArrayList;
import java.util.List;

public class ChartDescribeActivity extends AppCompatActivity {

    public static final String BUNDLE = "bundle";
    public static final String CHART = "chart";
    public static final String CHART_TYPE = "chart_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_describe);
        final TextView dText = findViewById(R.id.d_text);
        final Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle != null) {
            final ChartTypeEnum chartType = (ChartTypeEnum) bundle.getSerializable(CHART_TYPE);
            switch (chartType) {
                case PIE: {
                    final PieChart pieChart = (PieChart) bundle.getSerializable(CHART);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Loại biểu đồ: " + pieChart.getDescription());
                    stringBuilder.append("\nTên: " + pieChart.getChartName());
                    final ArrayList<SimpleInputRow> inputRows = pieChart.getData();
                    final SimpleInputRow header = inputRows.get(0);//dòng đầu
                    final List<SimpleInputRow> inputRowsNoHeader = inputRows.subList(1, inputRows.size());//còn lại
                    stringBuilder.append("\nSố hạng mục: " + inputRowsNoHeader.size());
                    stringBuilder.append("\nTên nhóm hạng mục: " + header.getLabel());
                    stringBuilder.append("\nTên nhóm giá trị: " + header.getValue());
                    stringBuilder.append("\nCác hạng mục: ");
                    for (int i = 0; i < inputRowsNoHeader.size(); i++) {
                        stringBuilder.append(inputRowsNoHeader.get(i).getLabel() + ", ");
                    }
                    stringBuilder.append("\nCác giá trị: ");
                    for (int i = 0; i < inputRowsNoHeader.size(); i++) {
                        stringBuilder.append(inputRowsNoHeader.get(i).getValue() + ", ");
                    }
                    dText.setText(stringBuilder.toString());
                }
                break;
            }
        }


    }
}
