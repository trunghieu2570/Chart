package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class LineChart extends Chart implements Serializable {

    private ArrayList<AdvancedInputRow> mData;
    private String xAxisUnit;
    private String yAxisUnit;
    private String yAxisUnitMeaning;
    private String objectMeaning;

    public LineChart(String chartName, String description, String xAxisUnit, String yAxisUnit, String yAxisUnitMeaning, String objectMeaning) {
        super(chartName, description);
        this.xAxisUnit = xAxisUnit;
        this.yAxisUnit = yAxisUnit;
        this.yAxisUnitMeaning = yAxisUnitMeaning;
        this.objectMeaning = objectMeaning;
    }

    public String getObjectMeaning() {
        return this.objectMeaning;
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

    public String getyAxisUnitMeaning() {
        return yAxisUnitMeaning;
    }
}
