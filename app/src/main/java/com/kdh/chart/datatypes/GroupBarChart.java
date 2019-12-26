package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupBarChart extends Chart implements Serializable {
    private ArrayList<AdvancedInputRow> mData;
    private String xAxisUnit;
    private String yAxisUnit;
    private String yAxisUnitMeaning;
    private String objectMeaning;

    public GroupBarChart(String chartName, String description, String xAxisUnit, String yAxisUnit, String yAxisUnitMeaning, String objectMeaning) {

        super(chartName, description);
        this.xAxisUnit = xAxisUnit;
        this.yAxisUnit = yAxisUnit;
        this.yAxisUnitMeaning = yAxisUnitMeaning;
        this.objectMeaning = objectMeaning;
    }

    public String getxAxisUnit() {
        return xAxisUnit;
    }

    public String getyAxisUnit() {
        return yAxisUnit;
    }

    public String getyAxisUnitMeaning() {
        return yAxisUnitMeaning;
    }

    public String getObjectMeaning() {
        return objectMeaning;
    }

    public ArrayList<AdvancedInputRow> getData() {
        return mData;
    }

    public void setData(ArrayList<AdvancedInputRow> mData) {
        this.mData = mData;
    }
}