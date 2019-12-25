package com.kdh.chart.activities;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdh.chart.R;
import com.kdh.chart.datatypes.AdvancedInputRow;
import com.kdh.chart.datatypes.DonutChart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatisticDonutChart extends AppCompatActivity {

    //các trường cần thiết dùng cho phân tích
    String chartName;
    String valueName;
    String valueMeaning;
    String[] values;
    String[] times;
    String timeMeaning;
    String fieldName;
    String[] fields;
    //Những dòng phân tích
    TextView txt_Name_donut;
    TextView txt_TongThe_donut;
    TextView txt_nam_donut;
    TextView txt_MaxIncrease_donut;
    TextView txt_MaxIncObj_donut;
    TextView txt_IncXuHuong_donut;
    TextView txt_MaxDecrease_donut;
    TextView txt_MaxDecObj_donut;
    TextView txt_DecXuHuong_donut;
    LinearLayout layout_donut;

    public static final String BUNDLE = "bundle";
    public static final String CHART = "chart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_donut_chart);
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

    private float caculateTheTang()
    {
        float res=0;

        float sum1=0;
        float sum2=0;
        for(int i=0;i<values.length;i++)
        {
            String[] valueStr=values[i].split(" ");
            sum1+=Float.parseFloat(valueStr[0]);
            sum2+=Float.parseFloat(valueStr[valueStr.length-1]);
        }
        if(sum1>sum2) {
            res=-((1-sum2/sum1)*100);
        }
        if(sum1<sum2) {
            res=(sum2/sum1-1)*100;
        }
        return res;
    }

    private void Statistic()
    {
        DecimalFormat df = new DecimalFormat("##.##");
        txt_Name_donut.setText("Nhận xét "+chartName);
        //=================================================
        float temp1=caculateTheTang();
        String resStr="";
        if(temp1==0) resStr=" không thay đổi";
        else
        if(temp1<0) resStr=" giảm "+df.format(-temp1)+"%";
        else resStr=" tăng "+df.format(temp1)+"%";
        txt_TongThe_donut.setText("Tổng thể: "+valueMeaning.toLowerCase()+" từ "+timeMeaning.toLowerCase()+" "+times[0]+" đến "+timeMeaning.toLowerCase()+" "+times[times.length-1].toLowerCase()+resStr.toLowerCase());
        //=================================================
        txt_nam_donut.setText("Từ "+timeMeaning.toLowerCase()+" "+times[0]+" đến "+timeMeaning.toLowerCase()+" "+times[times.length-1].toLowerCase()+":");
        //=================================================
        String maxInc=findMaxIncrease();
        if(maxInc.equals(""))
        {
            txt_MaxIncrease_donut.setHeight(0);
            txt_MaxIncObj_donut.setHeight(0);
            txt_IncXuHuong_donut.setHeight(0);
        }
        else
        {
            txt_MaxIncrease_donut.setText("-Đối tượng tăng nhiều nhất:");
            String[] temp2=maxInc.split(";");
            txt_MaxIncObj_donut.setText("+"+fields[Integer.parseInt(temp2[0])]+", tăng "+temp2[1]+"%");
            txt_IncXuHuong_donut.setText("+Xu hướng: "+temp2[2]);
        }
        //=================================================
        String maxDec=findMaxDecrease();
        if(maxDec.equals(""))
        {
            txt_MaxDecrease_donut.setHeight(0);
            txt_MaxDecObj_donut.setHeight(0);
            txt_DecXuHuong_donut.setHeight(0);
        }
        else
        {
            txt_MaxDecrease_donut.setText("-Đối tượng giảm nhiều nhất:");
            String[] temp2=maxDec.split(";");
            txt_MaxDecObj_donut.setText("+"+fields[Integer.parseInt(temp2[0])]+", giảm "+temp2[1]+"%");
            txt_DecXuHuong_donut.setText("+Xu hướng: "+temp2[2]);
        }

        //=================================================
        //layout_donut
        for(int i=0;i<times.length;i++)
        {
            addTextVeiw("-"+timeMeaning+" "+times[i]+":",20,60);
            //xét max và second giống nhau:
            float maxPos=Float.parseFloat(findMax(i).split(";")[1]);
            float secPos=Float.parseFloat(findSecond(i).split(";")[1]);
            if(maxPos!=secPos)
            {
                //max
                String[] tempMax=findMax(i).split(";");
                String text1="+"+fieldName+" có "+valueMeaning.toLowerCase()+" nhiều nhất là: "+fields[Integer.parseInt(tempMax[0])]+" ("+tempMax[1]+" "+valueName.toLowerCase()+", chiếm "+tempMax[2]+"%)";
                addTextVeiw(text1,20,120);
                //second
                if(fields.length>2)
                {
                    String[] tempSec=findSecond(i).split(";");
                    String text3="+"+fieldName+" có "+valueMeaning.toLowerCase()+" nhiều nhì là: "+fields[Integer.parseInt(tempSec[0])]+" ("+tempSec[1]+" "+valueName.toLowerCase()+", chiếm "+tempSec[2]+"%)";
                    addTextVeiw(text3,20,120);
                }
            }
            else
            {
                String[] tempMax=findMax(i).split(";");
                String[] tempSec=findSecond(i).split(";");
                String text1="+Hai "+fieldName.toLowerCase()+" có "+valueMeaning.toLowerCase()+" nhiều nhất là: "+fields[Integer.parseInt(tempMax[0])]+" và "+fields[Integer.parseInt(tempSec[0])]+" ("+tempMax[1]+" "+valueName.toLowerCase()+", chiếm "+tempMax[2]+"%)";
                addTextVeiw(text1,20,120);
            }

            //min
            String[] tempMin=findMin(i).split(";");
            String text2="+"+fieldName+" có "+valueMeaning.toLowerCase()+" ít nhất là: "+fields[Integer.parseInt(tempMin[0])]+" ("+tempMin[1]+" "+valueName.toLowerCase()+", chiếm "+tempMin[2]+"%)";
            addTextVeiw(text2,20,120);
        }

    }

    private String findSecond(int year)
    {
        String res="";
        int maxPos=Integer.parseInt(findMax(year).split(";")[0]);
        int secPos=-1;
        float second=0;

        float[] data=new float[fields.length];
        float sum=0;
        for(int i=0;i<data.length;i++)
        {
            String[] temp=values[i].split(" ");
            data[i]=Float.parseFloat(temp[year]);
            sum+=data[i];
        }

        //find second in data[]
        for(int i=0;i<data.length;i++)
            if(i!=maxPos)
            {
                if(secPos==-1)
                {
                    secPos=i;
                    second=data[i];
                }
                else
                {
                    if(data[i]>second)
                    {
                        second=data[i];
                        secPos=i;
                    }
                }
            }
        DecimalFormat df = new DecimalFormat("##.##");
        res=String.valueOf(secPos)+";"+String.valueOf(second)+";"+df.format(second*100/sum);

        return res;
    }

    private String findMin(int year)
    {
        String res="";
        float[] data=new float[fields.length];
        float sum=0;
        for(int i=0;i<data.length;i++)
        {
            String[] temp=values[i].split(" ");
            data[i]=Float.parseFloat(temp[year]);
            sum+=data[i];
        }

        //find min in data[]
        int minPos=-1;
        float min=0;
        for(int i=0;i<data.length;i++)
        {
            if(minPos==-1)
            {
                minPos=i;
                min=data[i];
            }
            else
            {
                if(min>data[i])
                {
                    min=data[i];
                    minPos=i;
                }
            }
        }
        DecimalFormat df = new DecimalFormat("##.##");
        res=String.valueOf(minPos)+";"+String.valueOf(min)+";"+df.format(min*100/sum);

        return res;
    }

    private String findMax(int year)
    {
        String res="";
        float[] data=new float[fields.length];
        float sum=0;
        for(int i=0;i<data.length;i++)
        {
            String[] temp=values[i].split(" ");
            data[i]=Float.parseFloat(temp[year]);
            sum+=data[i];
        }

        //find max in data[]
        int maxPos=-1;
        float max=0;
        for(int i=0;i<data.length;i++)
        {
            if(maxPos==-1)
            {
                maxPos=i;
                max=data[i];
            }
            else
            {
                if(max<data[i])
                {
                    max=data[i];
                    maxPos=i;
                }
            }
        }
        DecimalFormat df = new DecimalFormat("##.##");
        res=String.valueOf(maxPos)+";"+String.valueOf(max)+";"+df.format(max*100/sum);

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
        layout_donut.addView(x);
    }

    private int checkLienTuc(String dataStr, boolean isTang)     //-x: không liên tục tại vị trí x, nếu liên tục, trả về 1
    {

        String[] temp=dataStr.split(" ");
        float[]data=new float[temp.length];
        for(int i=0;i<temp.length;i++)
            data[i]=Float.parseFloat(temp[i]);

        int res = 1;
        for (int i = 1; i < data.length; i++) {
            if (isTang && data[i] < data[i - 1]) return -i;
            if (!isTang && data[i] > data[i - 1]) return -i;
        }

        return res;
    }

    private String findMaxIncrease()
    {
        DecimalFormat df = new DecimalFormat("##.##");
        String res="";
        int maxPos=-1;
        float max=0;
        float saveFirst=0;
        float saveLast=0;
        for(int i=0;i<values.length;i++)
        {
            String[] valueStr=values[i].split(" ");
            float first=Float.parseFloat(valueStr[0]);
            float last=Float.parseFloat(valueStr[valueStr.length-1]);
            if(maxPos==-1)
            {
                maxPos=i;
                max=last-first;
                saveFirst=first;
                saveLast=last;
            }
            else
            {
                if(last-first>max)
                {
                    max=last-first;
                    maxPos=i;
                    saveFirst=first;
                    saveLast=last;
                }
            }
        }
        if(max>0)
        {
            res= String.valueOf(maxPos)+";"+df.format((saveLast/saveFirst-1)*100);
            if(checkLienTuc(values[maxPos],true)==1) res+=";tăng liên tục";
            else res+=";tăng không liên tục";
        }

        return res;
    }

    private String findMaxDecrease()
    {
        DecimalFormat df = new DecimalFormat("##.##");
        String res="";
        int minPos=-1;
        float min=0;
        float saveFirst=0;
        float saveLast=0;
        for(int i=0;i<values.length;i++)
        {
            String[] valueStr=values[i].split(" ");
            float first=Float.parseFloat(valueStr[0]);
            float last=Float.parseFloat(valueStr[valueStr.length-1]);
            if(minPos==-1)
            {
                minPos=i;
                min=last-first;
                saveFirst=first;
                saveLast=last;
            }
            else
            {
                if(last-first<min)
                {
                    min=last-first;
                    minPos=i;
                    saveFirst=first;
                    saveLast=last;
                }
            }
        }
        if(min<0)
        {
            res= String.valueOf(minPos)+";"+df.format((1-saveLast/saveFirst)*100);
            if(checkLienTuc(values[minPos],false)==1) res+=";giảm liên tục";
            else res+=";giảm không liên tục";
        }
        return res;
    }

    private void getInputData()
    {
//        chartName="Sản lượng gạo các năm";
//        valueMeaning="Sản lượng";
//        valueName="Tấn";
//        values=new String[]{"6 1 2 5","2 5 5 4","1 6 5 9"}; //dãy vl obj1,obj2,...
//        times=new String[]{"2001","2002","2003","2004"};
//        timeMeaning="Năm";
//        fieldName="Nước";
//        fields=new String[]{"Việt Nam","Hàn Quốc","Singapore"};



        final Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        if (bundle == null) return;

        final DonutChart donutChart = (DonutChart) bundle.getSerializable(CHART);
        final ArrayList<AdvancedInputRow> inputRows = donutChart.getData();
        final AdvancedInputRow header = inputRows.get(0);//dòng đầu
        final List<AdvancedInputRow> inputRowsNoHeader = inputRows.subList(1, inputRows.size());//còn lại


        chartName = donutChart.getChartName();
        valueName=donutChart.getValueName();
        valueMeaning = donutChart.getValuesMeaning();//
        timeMeaning=donutChart.getTimeName();
        fieldName=donutChart.getObjMeaning();

        //get timelines
        times = convertListToArrayString(header.getValues());


        //get name of objects
        String[] temp = new String[inputRowsNoHeader.size()];
        for (int i = 0; i < temp.length; i++)
            temp[i] = inputRowsNoHeader.get(i).getLabel();

        fields = temp;

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
        values = temp1;
    }

    private String[] convertListToArrayString(List<String> list) {
        String[] temp = new String[list.size()];
        for (int i = 0; i < temp.length; i++)
            temp[i] = list.get(i);
        return temp;
    }

    private void getID()
    {
        txt_Name_donut=findViewById(R.id.txt_Name_donut);
        txt_TongThe_donut=findViewById(R.id.txt_TongThe_donut);
        txt_nam_donut=findViewById(R.id.txt_nam_donut);
        txt_MaxIncrease_donut=findViewById(R.id.txt_MaxIncrease_donut);
        txt_MaxIncObj_donut=findViewById(R.id.txt_MaxIncObj_donut);
        txt_IncXuHuong_donut=findViewById(R.id.txt_IncXuHuong_donut);
        txt_MaxDecrease_donut=findViewById(R.id.txt_MaxDecrease_donut);
        txt_MaxDecObj_donut=findViewById(R.id.txt_MaxDecObj_donut);
        txt_DecXuHuong_donut=findViewById(R.id.txt_DecXuHuong_donut);
        layout_donut=findViewById(R.id.layout_donut);
    }
}
