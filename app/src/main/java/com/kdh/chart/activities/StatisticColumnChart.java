package com.kdh.chart.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kdh.chart.R;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.ColumnBarChart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatisticColumnChart extends AppCompatActivity {

    public static final String BUNDLE = "bundle";
    public static final String CHART = "chart";
    //DATA from chart
    private String name;
    private String unitYName; //đơn vị trục tung (tấn, %,..)
    private String valueName; //vd: Sản lượng, số lượng,...
    private String unitXName; //đơn vị trục hoành (năm,tháng,..)
    private String[] timeLines; //mốc thời gian trục hoành
    private String[] values;
    private String fieldName; //Ý nghĩa của đối tượng đối tượng
    private String[] fields;  //tên của từng đối tượng
    private TextView txt_Name_column;
    private TextView txt_TongThe_column;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_column_chart);

        getID();
        getInputData();
        Statistic();
    }

    private float[] convertStringToFloatArr(String data) {
        String[] temp = data.split(" ");
        float[] res = new float[temp.length];
        for (int i = 0; i < temp.length; i++) {
            res[i] = Float.parseFloat(temp[i]);
        }
        return res;
    }

    private String caculateTongThe() {
        DecimalFormat df = new DecimalFormat("##.##");
        String res = "";
        float[] temp1 = convertStringToFloatArr(values[0]);
        float[] temp2 = convertStringToFloatArr(values[values.length - 1]);
        float sum1 = 0;
        float sum2 = 0;
        for (int i = 0; i < temp1.length; i++) {
            sum1 += temp1[i];
            sum2 += temp2[i];
        }
        if (sum1 == sum2)
            res = "không thay đổi";
        else if (sum1 > sum2)
            res = "giảm " + df.format((1 - sum2 / sum1) * 100) + "%";
        else
            res = "tăng " + df.format((sum2 / sum1 - 1) * 100) + "%";
        return res;
    }

    private void addTextVeiw(String text, int size, int mar) {
        TextView x = new TextView(this);
        x.setTextSize(size);
        x.setText(text);
        x.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(mar, 0, 60, 0);
        x.setLayoutParams(params);

        layout.addView(x);

    }

    private void caculateMax(int year) {
        DecimalFormat df = new DecimalFormat("##.##");
        float[] yearValues = convertStringToFloatArr(values[year]);
        float average = 0;
        int maxPos = -1;
        float max = 0;
        for (int i = 0; i < yearValues.length; i++) {
            average += yearValues[i];
            if (maxPos == -1) {
                maxPos = i;
                max = yearValues[i];
            } else {
                if (max < yearValues[i]) {
                    maxPos = i;
                    max = yearValues[i];
                }
            }
        }
        average /= yearValues.length;
        addTextVeiw("*" + fieldName + " " + fields[maxPos], 20, 120);
        String temp = "*Cao hơn " + df.format((max / average - 1) * 100) + "% so với " + valueName.toLowerCase() + " trung bình";
        addTextVeiw(temp, 20, 120);
    }

    private void caculateMin(int year) {
        DecimalFormat df = new DecimalFormat("##.##");
        float[] yearValues = convertStringToFloatArr(values[year]);
        float average = 0;
        int minPos = -1;
        float min = 0;
        for (int i = 0; i < yearValues.length; i++) {
            average += yearValues[i];
            if (minPos == -1) {
                minPos = i;
                min = yearValues[i];
            } else {
                if (min > yearValues[i]) {
                    minPos = i;
                    min = yearValues[i];
                }
            }
        }
        average /= yearValues.length;
        addTextVeiw("*" + fieldName + " " + fields[minPos], 20, 120);
        String temp = "*Thấp hơn " + df.format((1 - min / average) * 100) + "% so với " + valueName.toLowerCase() + " trung bình";
        addTextVeiw(temp, 20, 120);
    }

    private void Statistic() {
        txt_Name_column.setText("Nhận xét ".toUpperCase() + name.toUpperCase());
        txt_TongThe_column.setText("Tổng thể: " + valueName + " " + caculateTongThe() + " từ " + unitXName.toLowerCase() + " " + timeLines[0] + " đến " + unitXName.toLowerCase() + " " + timeLines[timeLines.length - 1]);
        for (int i = 0; i < timeLines.length; i++) {
            addTextVeiw("-" + unitXName + " " + timeLines[i] + ":", 20, 40);
            addTextVeiw("+" + fieldName + " chiếm " + valueName.toLowerCase() + " lớn nhất là: ", 20, 80);
            caculateMax(i);
            addTextVeiw("+" + fieldName + " chiếm " + valueName.toLowerCase() + " nhỏ nhất là: ", 20, 80);
            caculateMin(i);
        }
    }


    private void getInputData() {
//        name="Sản lượng lúa các quốc gia theo các năm";
//        unitYName="Tấn"; //đơn vị trục tung (tấn, %,..)
//        valueName="Sản lượng"; //vd: Sản lượng, số lượng,...
//
//        unitXName="Năm"; //đơn vị trục hoành (năm,tháng,..)
//        timeLines=new String[]{"2000", "2001", "2002"}; //mốc thời gian trục hoành
//
//        values=new String[]{"2.5 2.5 1","2 2 3","3 1 1"}; //values theo "nhóm nước 1","nhóm nước 2",... theo thứ tự từ dưới lên
//        fieldName="Nước"; //Ý nghĩa của đối tượng đối tượng
//        fields=new String[]{"Việt Nam","Singapore","Hàn Quốc"};  //tên của từng đối tượng

        final Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle == null) return;

        final ColumnBarChart columnChart = (ColumnBarChart) bundle.getSerializable(CHART);
        final ArrayList<AdvancedInputRow> inputRows = columnChart.getData();
        final AdvancedInputRow header = inputRows.get(0);//dòng đầu
        final List<AdvancedInputRow> inputRowsNoHeader = inputRows.subList(1, inputRows.size());//còn lại


        name = columnChart.getChartName();
        unitYName = columnChart.getyAxisUnit();
        valueName = columnChart.getyAxisUnitMeaning();//

        unitXName = columnChart.getxAxisUnit();
        //get timelines
        timeLines = convertListToArrayString(header.getValues());


        fieldName = columnChart.getObjectMeaning();//
        //get name of objects
        String[] temp = new String[inputRowsNoHeader.size()];
        for (int i = 0; i < temp.length; i++)
            temp[i] = inputRowsNoHeader.get(i).getLabel();

        fields = temp;                    //new String[]{"Lào","Thái Lan","Mỹ","Việt Nam"};

        //get values
        String[] temp1 = new String[inputRowsNoHeader.size()];
        for (int i = 0; i < temp.length; i++) {
            List<String> valueI = inputRowsNoHeader.get(i).getValues();
            String connect = "";
            for (int j = 0; j < valueI.size(); j++) {
                connect += valueI.get(j);
                if (j < valueI.size() - 1) connect += " ";
            }

            temp1[i] = connect;
        }
        values = temp1;   // new String[]{"1 1.5 2 3 3", "6 6 5 5 3.5","3 5 6 5.5 6","7 4 3 0.5 8"};
    }

    private String[] convertListToArrayString(List<String> list) {
        String[] temp = new String[list.size()];
        for (int i = 0; i < temp.length; i++)
            temp[i] = list.get(i);
        return temp;
    }

    private void getID() {
        txt_Name_column = findViewById(R.id.txt_Name_column);
        txt_TongThe_column = findViewById(R.id.txt_TongThe_column);
        layout = findViewById(R.id.layout_column);
    }
}
