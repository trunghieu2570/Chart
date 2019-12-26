package com.kdh.chart.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kdh.chart.R;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.GroupBarChart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatisticGroupChart extends AppCompatActivity {

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
    private TextView txt_Name_grouped;
    private TextView txt_TongThe_grouped;
    private LinearLayout layout1;
    private LinearLayout layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_group_chart);
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

    private float[] convertStringToFloatArr(String data) {
        String[] temp = data.split(" ");
        float[] res = new float[temp.length];
        for (int i = 0; i < temp.length; i++) {
            res[i] = Float.parseFloat(temp[i]);
        }
        return res;
    }

    private String calculateTongThe() {
        DecimalFormat df = new DecimalFormat("##.##");
        String res = "";
        float sum1 = 0;
        float sum2 = 0;
        for (int i = 0; i < values.length; i++) {
            float[] temp = convertStringToFloatArr(values[i]);
            sum1 += temp[0];
            sum2 += temp[temp.length - 1];
        }


        if (sum1 == sum2) res = "không thay đổi";
        else if (sum1 > sum2) {
            res = "giảm ";
            res += String.format("%s%%", df.format((1 - sum2 / sum1) * 100));
        } else {
            res = "tăng ";
            res += String.format("%s%%", df.format((sum2 / sum1 - 1) * 100));
        }
        return res;
    }

    private void addTextView(String text, int size, int mar, int id) {
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
        if (id == 1)
            layout1.addView(x);
        else
            layout2.addView(x);
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

    private float[] getYearValues(int year) {
        float[] res = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            float[] temp = convertStringToFloatArr(values[i]);
            res[i] = temp[year];
        }
        return res;
    }

    private String findMax(float[] data) {
        DecimalFormat df = new DecimalFormat("##.##");
        String res = "";
        int maxPos = -1;
        float max = 0;
        float sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
            if (maxPos == -1) {
                maxPos = i;
                max = data[i];
            } else {
                if (max < data[i]) {
                    max = data[i];
                    maxPos = i;
                }
            }
        }
        res = String.format("%s, chiếm %s%%", fields[maxPos], df.format(max * 100 / sum));

        return res;
    }

    private String findMin(float[] data) {
        DecimalFormat df = new DecimalFormat("##.##");
        String res = "";
        int minPos = -1;
        float min = 0;
        float sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
            if (minPos == -1) {
                minPos = i;
                min = data[i];
            } else {
                if (min > data[i]) {
                    min = data[i];
                    minPos = i;
                }
            }
        }
        res = fields[minPos] + ", chiếm " + df.format(min * 100 / sum) + "%";

        return res;
    }

    private void Statistic() {
        DecimalFormat df = new DecimalFormat("##.##");
        txt_Name_grouped.setText(String.format("Nhận xét %s", name.toLowerCase()));
        txt_TongThe_grouped.setText(String.format("-Từ %s %s đến %s %s: %s tổng thể %s", unitXName.toLowerCase(), timeLines[0], unitXName.toLowerCase(), timeLines[1], valueName.toLowerCase(), calculateTongThe()));
        //====================================================================================
        for (int i = 0; i < fields.length; i++) {
            addTextView("-" + fields[i] + ":", 20, 40, 1);
            float[] data = convertStringToFloatArr(values[i]);
            String str = "";
            if (data[data.length - 1] > data[0])
                str = String.format("+ Từ %s %s đến %s %s: %s tăng %s%%", unitXName.toLowerCase(), timeLines[0], unitXName.toLowerCase(), timeLines[timeLines.length - 1], valueName.toLowerCase(), df.format(((data[data.length - 1] / data[0]) - 1) * 100));
            else
                str = String.format("+ Từ %s %s đến %s %s: %s giảm %s%%", unitXName.toLowerCase(), timeLines[0], unitXName.toLowerCase(), timeLines[timeLines.length - 1], valueName.toLowerCase(), df.format(((1 - data[data.length - 1] / data[0])) * 100));
            addTextView(str, 20, 120, 1);


            //nhận xét liên tục:
            str = "+ Xu hướng: ";
            String str1 = ""; //nhận xét xu hướng liên tục
            String str2 = "";
            if (data[data.length - 1] > data[0]) {
                //tăng
                if (checkLienTuc(data, true) == 1) {
                    str += "Tăng liên tục:";
                    int max = findMaxLienTuc(data, true);
                    int min = findMinLienTuc(data, true);
                    str1 = String.format("*Giai đoạn tăng nhanh nhất: từ %s %s đến %s %s, tăng %s%%", unitXName.toLowerCase(), timeLines[max], unitXName.toLowerCase(), timeLines[max + 1], df.format((data[max + 1] / data[max] - 1) * 100));
                    str2 = String.format("*Giai đoạn tăng chậm nhất: từ %s %s đến %s %s, tăng %s%%", unitXName.toLowerCase(), timeLines[min], unitXName.toLowerCase(), timeLines[min + 1], df.format((data[min + 1] / data[min] - 1) * 100));

                } else {
                    str += "Tăng không liên tục:";
                    int breakPos = -checkLienTuc(data, true);
                    str1 = String.format("*Bắt đầu tăng không liên tục từ giai đoạn đoạn: %s %s đến %s %s, giảm %s%%", unitXName.toLowerCase(), timeLines[breakPos - 1], unitXName.toLowerCase(), timeLines[breakPos], df.format((1 - data[breakPos] / data[breakPos - 1]) * 100));
                }

            } else {
                //giảm
                if (checkLienTuc(data, false) == 1) {
                    str += "Giảm liên tục:";
                    int max = findMaxLienTuc(data, false);
                    int min = findMinLienTuc(data, false);
                    str1 = String.format("*Giai đoạn giảm nhanh nhất: từ %s %s đến %s %s, giảm %s%%", unitXName.toLowerCase(), timeLines[max], unitXName.toLowerCase(), timeLines[max + 1], df.format((1 - data[max + 1] / data[max]) * 100));
                    str2 = String.format("*Giai đoạn giảm chậm nhất: từ %s %s đến %s %s, giảm %s%%", unitXName.toLowerCase(), timeLines[min], unitXName.toLowerCase(), timeLines[min + 1], df.format((1 - data[min + 1] / data[min]) * 100));
                } else {
                    str += "Giảm không liên tục:";
                    int breakPos = -checkLienTuc(data, false);
                    str1 = String.format("*Bắt đầu giảm không liên tục từ giai đoạn đoạn: %s %s đến %s %s, tăng %s%%", unitXName.toLowerCase(), timeLines[breakPos - 1], unitXName.toLowerCase(), timeLines[breakPos], df.format((data[breakPos] / data[breakPos - 1] - 1) * 100));
                }
            }
            addTextView(str, 20, 120, 1);
            if (!str1.equals("")) addTextView(str1, 20, 200, 1);
            if (!str2.equals("")) addTextView(str2, 20, 200, 1);

        }
        //====================================================================================
        for (int i = 0; i < timeLines.length; i++) {
            addTextView(String.format("-%s %s:", unitXName, timeLines[i]), 20, 40, 2);
            float[] objValues = getYearValues(i);
            String max = String.format("+%s có %s lớn nhất là: %s", fieldName, valueName.toLowerCase(), findMax(objValues));
            addTextView(max, 20, 120, 2);
            String min = String.format("+%s có %s nhỏ nhất là: %s", fieldName, valueName.toLowerCase(), findMin(objValues));
            addTextView(min, 20, 120, 2);
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
//        values=new String[]{"5 2 4","2 2 1","1 6 5"}; //values theo "nước 1","nước 2",...
//        fieldName="Nước"; //Ý nghĩa của đối tượng đối tượng
//        fields=new String[]{"Việt Nam","Singapore","Hàn Quốc"};  //tên của từng đối tượng

        final Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle == null) return;

        final GroupBarChart groupChart = (GroupBarChart) bundle.getSerializable(CHART);
        final ArrayList<AdvancedInputRow> inputRows = groupChart.getData();
        final AdvancedInputRow header = inputRows.get(0);//dòng đầu
        final List<AdvancedInputRow> inputRowsNoHeader = inputRows.subList(1, inputRows.size());//còn lại


        name = groupChart.getChartName();
        unitYName = groupChart.getyAxisUnit();
        valueName = groupChart.getyAxisUnitMeaning();//

        unitXName = groupChart.getxAxisUnit();
        //get timelines
        timeLines = convertListToArrayString(header.getValues());


        fieldName = groupChart.getObjectMeaning();//
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
        txt_Name_grouped = findViewById(R.id.txt_Name_grouped);
        txt_TongThe_grouped = findViewById(R.id.txt_TongThe_grouped);
        layout1 = findViewById(R.id.layout1_grouped);
        layout2 = findViewById(R.id.layout2_grouped);
    }
}
