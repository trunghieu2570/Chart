package com.kdh.chart.datatypes;

import java.io.Serializable;

public class Chart implements Serializable {

    private String mChartName;
    private String mModifiedTime;
    private String mDescription;


    public Chart(String chartName, String description, String modifiedTime) {
        this.mChartName = chartName;
        this.mModifiedTime = modifiedTime;
        this.mDescription = description;
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
