package com.kdh.chart.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kdh.chart.R;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.LineChart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatisticLineChart extends AppCompatActivity {

    //DATA from chart
    private String name;
    private String unitYName; //đơn vị trục tung (tấn, %,..)
    private String valueName; //vd: Sản lượng, số lượng,...

    private String unitXName; //đơn vị trục hoành (năm,tháng,..)
    private String[] timeLines; //mốc thời gian trục hoành

    private String[] values;
    private String fieldName; //Ý nghĩa của đối tượng đối tượng
    private String[] fields;  //tên của từng đối tượng

    //Data caculate
    private float maxSpeed;
    private float minSpeed;
    private float duration;

    private TextView txt_Name_Line;
    private LinearLayout layout1;
    private TextView txt_TuongQuan_Line;
    private TextView txt_AverageSpeed_Line;
    private TextView txt_MaxSpeed_Line;
    private TextView txt_MaxAverage_Line;
    private TextView txt_MinSpeed_Line;
    private TextView txt_MinAverage_Line;

    public static final String BUNDLE = "bundle";
    public static final String CHART = "chart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_line_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        setTitle(R.string.statistic_title);

        getID();
        getInputData();
        Statistic();
    }

    private void getID() {
        txt_Name_Line = findViewById(R.id.txt_Name_Line);
        layout1 = findViewById(R.id.layout1);
        txt_TuongQuan_Line = findViewById(R.id.txt_TuongQuan_Line);
        txt_AverageSpeed_Line = findViewById(R.id.txt_AverageSpeed_Line);
        txt_MaxSpeed_Line = findViewById(R.id.txt_MaxSpeed_Line);
        txt_MaxAverage_Line = findViewById(R.id.txt_MaxAverage_Line);
        txt_MinSpeed_Line = findViewById(R.id.txt_MinSpeed_Line);
        txt_MinAverage_Line = findViewById(R.id.txt_MinAverage_Line);
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
        layout1.addView(x);
    }

    private void getInputData() {
        final Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle == null) return;

        final LineChart lineChart = (LineChart) bundle.getSerializable(CHART);
        final ArrayList<AdvancedInputRow> inputRows = lineChart.getData();
        final AdvancedInputRow header = inputRows.get(0);//dòng đầu
        final List<AdvancedInputRow> inputRowsNoHeader = inputRows.subList(1, inputRows.size());//còn lại


        name = lineChart.getChartName();
        unitYName = lineChart.getyAxisUnit();
        valueName = lineChart.getyAxisUnitMeaning();//

        unitXName = lineChart.getxAxisUnit();
        //get timelines
        timeLines = convertListToArrayString(header.getValues());
        try {
            duration = Float.parseFloat(timeLines[timeLines.length - 1]) - Float.parseFloat(timeLines[0]);
        } catch (Exception e) {
            duration = 1;
        }


        fieldName = lineChart.getObjectMeaning();//
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

    private void Statistic() {
        DecimalFormat df = new DecimalFormat("##.##");
        for (int i = 0; i < fields.length; i++) {
            addTextVeiw("-" + fields[i] + ":", 20, 40);
            float[] data = convertStringToFloatArr(values[i]);
            String str = "";
            if (data[data.length - 1] > data[0])
                str = "+ Từ " + unitXName.toLowerCase() + " " + timeLines[0] + " đến " + unitXName.toLowerCase() + " " + timeLines[timeLines.length - 1] + ": " + valueName.toLowerCase() + " tăng " + df.format(((data[data.length - 1] / data[0]) - 1) * 100) + "%";
            else
                str = "+ Từ " + unitXName.toLowerCase() + " " + timeLines[0] + " đến " + unitXName.toLowerCase() + " " + timeLines[timeLines.length - 1] + ": " + valueName.toLowerCase() + " giảm " + df.format(((1 - data[data.length - 1] / data[0])) * 100) + "%";
            addTextVeiw(str, 20, 120);


            //nhận xét liên tục:
            str = "+ Xu hướng: ";
            String str1 = ""; //nhận xét xu hướng liên tục
            String str2 = "";
            Log.d("khoa", values[i]);
            if (data[data.length - 1] > data[0]) {
                //tăng
                if (checkLienTuc(data, true) == 1) {
                    str += "Tăng liên tục:";
                    int max = findMaxLienTuc(data, true);
                    int min = findMinLienTuc(data, true);
                    str1 = "*Giai đoạn tăng nhanh nhất: từ " + unitXName.toLowerCase() + " " + timeLines[max] + " đến " + unitXName.toLowerCase() + " " + timeLines[max + 1] + ", tăng " + df.format((data[max + 1] / data[max] - 1) * 100) + "%";
                    str2 = "*Giai đoạn tăng chậm nhất: từ " + unitXName.toLowerCase() + " " + timeLines[min] + " đến " + unitXName.toLowerCase() + " " + timeLines[min + 1] + ", tăng " + df.format((data[min + 1] / data[min] - 1) * 100) + "%";

                } else {
                    str += "Tăng không liên tục:";
                    int breakPos = -checkLienTuc(data, true);
                    str1 = "*Bắt đầu tăng không liên tục từ giai đoạn đoạn: " + unitXName.toLowerCase() + " " + timeLines[breakPos - 1] + " đến " + unitXName.toLowerCase() + " " + timeLines[breakPos] + ", giảm " + df.format((1 - data[breakPos] / data[breakPos - 1]) * 100) + "%";
                }

            } else {
                //giảm
                if (checkLienTuc(data, false) == 1) {
                    str += "Giảm liên tục:";
                    int max = findMaxLienTuc(data, false);
                    int min = findMinLienTuc(data, false);
                    str1 = "*Giai đoạn giảm nhanh nhất: từ " + unitXName.toLowerCase() + " " + timeLines[max] + " đến " + unitXName.toLowerCase() + " " + timeLines[max + 1] + ", giảm " + df.format((1 - data[max + 1] / data[max]) * 100) + "%";
                    str2 = "*Giai đoạn giảm chậm nhất: từ " + unitXName.toLowerCase() + " " + timeLines[min] + " đến " + unitXName.toLowerCase() + " " + timeLines[min + 1] + ", giảm " + df.format((1 - data[min + 1] / data[min]) * 100) + "%";
                } else {
                    str += "Giảm không liên tục:";
                    int breakPos = -checkLienTuc(data, false);
                    str1 = "*Bắt đầu giảm không liên tục từ giai đoạn đoạn: " + unitXName.toLowerCase() + " " + timeLines[breakPos - 1] + " đến " + unitXName.toLowerCase() + " " + timeLines[breakPos] + ", tăng " + df.format((1 - data[breakPos] / data[breakPos - 1]) * 100) + "%";
                }
            }
            addTextVeiw(str, 20, 120);
            if (!str1.equals("")) addTextVeiw(str1, 20, 200);
            if (!str2.equals("")) addTextVeiw(str2, 20, 200);

        }


        //====================================================================================

        int maxSpeedPos = getMaxSpeedPosition(values);
        int minSpeedPos = getMinSpeedPosition(values);


        txt_Name_Line.setText(String.format("Nhận xét %s", name));

        txt_TuongQuan_Line.setText(String.format("Tương quan giữa các %s:", fieldName.toLowerCase()));
        txt_AverageSpeed_Line.setText(String.format("Tốc độ tăng trưởng trung bình: %s %s/%s.", df.format(getAverageSpeed(values)), unitYName, unitXName));
        txt_MaxSpeed_Line.setText(String.format("%s có tốc độ tăng trưởng nhanh nhất là %s: %s %s/%s.", fieldName, fields[getMaxSpeedPosition(values)], df.format(maxSpeed), unitYName, unitXName));
        if (maxSpeed <= 0 || getAverageSpeed(values) <= 0) txt_MaxAverage_Line.setHeight(0);
        else
            txt_MaxAverage_Line.setText(String.format("=> Lớn hơn %s %% so với tốc độ tăng trưởng trung bình.", df.format((maxSpeed / getAverageSpeed(values) - 1) * 100)));

        txt_MinSpeed_Line.setText(String.format("%s có tốc độ tăng trưởng chậm nhất là %s: %s %s/%s.", fieldName, fields[getMinSpeedPosition(values)], df.format(minSpeed), unitYName, unitXName));
        if (minSpeed <= 0 || getAverageSpeed(values) <= 0) txt_MinAverage_Line.setHeight(0);
        else
            txt_MinAverage_Line.setText(String.format("=> Nhỏ hơn %s %% so với tốc độ tăng trưởng trung bình.", df.format((1 - getAverageSpeed(values) / minSpeed) * 100)));
    }

    private float[] convertStringToFloatArr(String data) {
        String[] temp = data.split(" ");
        float[] res = new float[temp.length];
        for (int i = 0; i < temp.length; i++) {
            res[i] = Float.parseFloat(temp[i]);
        }
        return res;
    }

    private int checkLienTuc(float[] data, boolean isTang)     //-x: không liên tục tại vị trí x, nếu liên tục, trả về 1
    {
        int res = 1;
        for (int i = 1; i < data.length; i++) {
            if (isTang && data[i] < data[i - 1]) return -i;
            if (!isTang && data[i] > data[i - 1]) return -i;
        }

        return res;
    }

    private int findMaxLienTuc(float[] data, boolean isTang) {
        int res = 0;
        if (isTang) {
            float maxValue = data[1] - data[0];
            for (int i = 1; i < data.length - 1; i++)
                if (data[i + 1] - data[i] > maxValue) {
                    maxValue = data[i + 1] - data[i];
                    res = i;
                }
        } else {
            float maxValue = data[0] - data[1];
            for (int i = 1; i < data.length - 1; i++)
                if (data[i] - data[i + 1] > maxValue) {
                    maxValue = data[i] - data[i + 1];
                    res = i;
                }
        }
        return res;
    }

    private int findMinLienTuc(float[] data, boolean isTang) {
        int res = 0;
        if (isTang) {
            float minValue = data[1] - data[0];
            for (int i = 1; i < data.length - 1; i++)
                if (data[i + 1] - data[i] < minValue) {
                    minValue = data[i + 1] - data[i];
                    res = i;
                }
        } else {
            float minValue = data[0] - data[1];
            for (int i = 1; i < data.length - 1; i++)
                if (data[i] - data[i + 1] < minValue) {
                    minValue = data[i] - data[i + 1];
                    res = i;
                }
        }
        return res;
    }


    private int getMaxSpeedPosition(String[] values) {
        int pos = 0;
        float[] speed = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            String[] temp = values[i].split(" ");
            float first = Float.parseFloat(temp[0]);
            float last = Float.parseFloat(temp[temp.length - 1]);
            speed[i] = (last - first) / (duration);
        }

        for (int i = 1; i < speed.length; i++) {
            if (speed[i] > speed[pos])
                pos = i;
        }
        maxSpeed = speed[pos];
        DecimalFormat df = new DecimalFormat("#.0");
        //maxSpeed=Float.parseFloat(df.format(maxSpeed));
        return pos;
    }

    private int getMinSpeedPosition(String[] values) {
        int pos = 0;
        float[] speed = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            String[] temp = values[i].split(" ");
            float first = Float.parseFloat(temp[0]);
            float last = Float.parseFloat(temp[temp.length - 1]);
            speed[i] = (last - first) / (duration);
        }

        for (int i = 1; i < speed.length; i++) {
            if (speed[i] < speed[pos])
                pos = i;
        }
        minSpeed = speed[pos];
        DecimalFormat df = new DecimalFormat("#.0");
        //minSpeed=Float.parseFloat(df.format(minSpeed));
        return pos;
    }

    private float getAverageSpeed(String[] values) {
        float res = 0;
        for (int i = 0; i < values.length; i++) {
            String[] temp = values[i].split(" ");
            float first = Float.parseFloat(temp[0]);
            float last = Float.parseFloat(temp[temp.length - 1]);
            res += (last - first) / (duration);

        }
        res = res / values.length;
        DecimalFormat df = new DecimalFormat("#.0");
        //res=Float.parseFloat(df.format(res));

        return res;
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
