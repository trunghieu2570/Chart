package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class LineChart extends Chart implements Serializable {

    private ArrayList<AdvancedInputRow> mData;
    private String xAxisUnit;
    private String yAxisUnit;

    public LineChart(String chartName, String description, String modifiedTime, String xAxisUnit, String yAxisUnit) {
        super(chartName, description, modifiedTime);
        this.xAxisUnit = xAxisUnit;
        this.yAxisUnit = yAxisUnit;
    }

    public ArrayList<AdvancedInputRow> getData() {
        return mData;
    }

    public void setData(ArrayList<AdvancedInputRow> mData) {
        this.mData = mData;
    }

    public String getxAxisUnit() {
        return xAxisUnit;
    }

    public void setxAxisUnit(String xAxisUnit) {
        this.xAxisUnit = xAxisUnit;
    }

    public void setyAxisUnit(String yAxisUnit) {
        this.yAxisUnit = yAxisUnit;
    }

    public String getyAxisUnit() {
        return yAxisUnit;
    }
}
