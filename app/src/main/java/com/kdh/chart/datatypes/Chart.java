package com.kdh.chart.datatypes;

import java.io.Serializable;

public class Chart implements Serializable {

    public enum ChartType {
        PIE,
        COLUMN,
        LINE,
        DONUT,
        GROUPED,
    }

    private String mChartName;
    private String mModifiedTime;
    private ChartType mType;
    private String mDescription;


    public Chart(String chartName, String description, String modifiedTime, ChartType chartType) {
        this.mChartName = chartName;
        this.mModifiedTime = modifiedTime;
        this.mType = chartType;
        this.mDescription = description;
    }

    public ChartType getType() {
        return mType;
    }

    private void setType(ChartType mType) {
        this.mType = mType;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getChartName() {
        return mChartName;
    }

    public String getModifiedTime() {
        return mModifiedTime;
    }

    public void setChartName(String mChartName) {
        this.mChartName = mChartName;
    }

    public void setModifiedTime(String mModifiedTime) {
        this.mModifiedTime = mModifiedTime;
    }
}
