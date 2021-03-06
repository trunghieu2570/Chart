package com.kdh.chart.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kdh.chart.R;
import com.kdh.chart.datatypes.PieChart;
import com.kdh.chart.datatypes.SimpleInputRow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatisticPieChart extends AppCompatActivity {

    //các trường cần thiết dùng cho phân tích
    String chartName;
    String valueName;
    float[] values;
    String fieldName;
    String[] fields;
    //những dòng phân tích
    TextView txt_Name;
    TextView txt_Max;
    TextView txt_Second;
    TextView txt_Min;
    TextView txt_TuongQuan;
    TextView txt_MaxSecond;
    TextView txt_MaxMin;
    TextView txt_MaxAverage;
    TextView txt_ChenhLech;
    TextView txt_Nhanxet;
    TextView txt_KetLuan;


    public static final String BUNDLE = "bundle";
    public static final String CHART = "chart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_pie_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        setTitle(R.string.statistic_title);

        getID();
        getInputData();
        Statistic();
        //final TextView dText = findViewById(R.id.d_text);
        /*final Bundle bundle = getIntent().getBundleExtra(BUNDLE);
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
                    //dText.setText(stringBuilder.toString());
                }
                break;
            }
        }
        */

    }

    private void getID() {

        txt_Name = findViewById(R.id.txt_Name_pie);
        txt_Max = findViewById(R.id.txt_Max_Pie);
        txt_Second = findViewById(R.id.txt_Second_Pie);
        txt_Min = findViewById(R.id.txt_Min_Pie);
        txt_TuongQuan = findViewById(R.id.txt_TuongQuan_Pie);
        txt_MaxSecond = findViewById(R.id.txt_MaxSecond_Pie);
        txt_MaxMin = findViewById(R.id.txt_MaxMin_Pie);
        txt_MaxAverage = findViewById(R.id.txt_MaxAverage_Pie);
        txt_ChenhLech = findViewById(R.id.txt_ChenhLech_Pie);
        txt_Nhanxet = findViewById(R.id.txt_Nhanxet_Pie);
        txt_KetLuan = findViewById(R.id.txt_KetLuan_Pie);
    }

    private void getInputData() {
        final Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle == null) return;

        final PieChart pieChart = (PieChart) bundle.getSerializable(CHART);

//        stringBuilder.append("Loại biểu đồ: " + pieChart.getDescription());
//        stringBuilder.append("\nTên: " + pieChart.getChartName());
//        final ArrayList<SimpleInputRow> inputRows = pieChart.getData();
//        final SimpleInputRow header = inputRows.get(0);//dòng đầu
//        final List<SimpleInputRow> inputRowsNoHeader = inputRows.subList(1, inputRows.size());//còn lại
//        stringBuilder.append("\nSố hạng mục: " + inputRowsNoHeader.size());
//        stringBuilder.append("\nTên nhóm hạng mục: " + header.getLabel());
//        stringBuilder.append("\nTên nhóm giá trị: " + header.getValue());
//        stringBuilder.append("\nCác hạng mục: ");
//        for (int i = 0; i < inputRowsNoHeader.size(); i++) {
//            stringBuilder.append(inputRowsNoHeader.get(i).getLabel() + ", ");
//        }
//        stringBuilder.append("\nCác giá trị: ");
//        for (int i = 0; i < inputRowsNoHeader.size(); i++) {
//            stringBuilder.append(inputRowsNoHeader.get(i).getValue() + ", ");
//        }


        final ArrayList<SimpleInputRow> inputRows = pieChart.getData();
        final SimpleInputRow header = inputRows.get(0);//dòng đầu
        final List<SimpleInputRow> inputRowsNoHeader = inputRows.subList(1, inputRows.size());//còn lại

        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("");
        for (int i = 0; i < inputRowsNoHeader.size(); i++) {
            stringBuilder1.append(inputRowsNoHeader.get(i).getValue() + " ");
        }
        values = getValuesFromString(stringBuilder1.toString());

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("");
        for (int i = 0; i < inputRowsNoHeader.size(); i++) {
            stringBuilder2.append(inputRowsNoHeader.get(i).getLabel() + "  ");
        }
        fields = (stringBuilder2.toString()).split("  ");

        chartName = pieChart.getChartName();
        valueName = header.getValue();
        fieldName = header.getLabel();

    }

    private float[] getValuesFromString(String valuesStr) {
        String[] temp = valuesStr.split(" ");
        float[] res = new float[temp.length];
        for (int i = 0; i < temp.length; i++) {
            res[i] = Float.parseFloat(temp[i]);
        }
        return res;
    }

    private void Statistic() {


        int max = getMaxPosition(values);
        int second = getSecondPosition(values);
        int min = getMinPosition(values);
        float average = getAverageValue(values);
        float chenhLech = (values[max] / values[min] - 1) * 100;
        DecimalFormat df1 = new DecimalFormat("#.00");
        DecimalFormat df2 = new DecimalFormat("#.0");
        txt_Name.setText(String.format("Nhận xét %s", chartName));
        txt_Max.setText(String.format("- %s là %s có %s cao nhất.", fields[max], fieldName.toLowerCase(), valueName.toLowerCase()));
        if (second == min) {
            txt_Second.setHeight(0);
            txt_MaxSecond.setHeight(0);
        }
//        else
//        {
//            txt_Second.setHeight(80);
//            txt_MaxSecond.setHeight(80);
//        }
        txt_Second.setText(String.format("- %s là %s có %s cao nhì.", fields[second], fieldName.toLowerCase(), valueName.toLowerCase()));
        txt_Min.setText(String.format("- %s là %s có %s thấp nhất.", fields[min], fieldName.toLowerCase(), valueName.toLowerCase()));
        txt_TuongQuan.setText(String.format("Tương quan về %s:", valueName.toLowerCase()));
        txt_MaxSecond.setText(String.format("- %s %s thứ nhất gấp %s lần %s thứ nhì.", valueName, fieldName.toLowerCase(), df1.format(values[max] / values[second]), fieldName.toLowerCase()));
        txt_MaxMin.setText(String.format("- %s %s cao nhất gấp %s lần %s thấp nhất.", valueName, fieldName.toLowerCase(), df1.format(values[max] / values[min]), fieldName.toLowerCase()));
        txt_MaxAverage.setText(String.format("- %s %s cao nhất gấp %s lần so với %s trung bình chung.", valueName, fieldName.toLowerCase(), df1.format(values[max] / average), valueName.toLowerCase()));
        txt_Nhanxet.setText(String.format("Chênh lệch %s giữa %s cao nhất và thấp nhất là: %s%%", valueName.toLowerCase(), fieldName.toLowerCase(), df2.format(chenhLech)));
        String KetLuan = "";
        if (chenhLech >= 50)
            KetLuan = String.format("=>%s không đồng đều giữa các %s.", valueName, fieldName);
        else KetLuan = String.format("%s đồng đều giữa các %s.", valueName, fieldName);
        txt_KetLuan.setText(KetLuan);

    }

    private float getAverageValue(float[] data) {
        float aver = 0;
        for (float x : data)
            aver += x;

        return aver / data.length;
    }

    private int getMaxPosition(float[] data) {
        int pos = 0;
        for (int i = 1; i < data.length; i++)
            if (data[i] > data[pos])
                pos = i;

        return pos;
    }

    private int getMinPosition(float[] data) {
        int pos = 0;
        for (int i = 1; i < data.length; i++)
            if (data[i] < data[pos])
                pos = i;

        return pos;
    }

    private int getSecondPosition(float[] data) {
        int max = getMaxPosition(data);
        int pos = -1;
        for (int i = 0; i < data.length; i++) {
            if (pos == -1) {
                if (i != max) pos = i;
            } else if (i != max && data[i] > data[pos])
                pos = i;
        }


        return pos;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
