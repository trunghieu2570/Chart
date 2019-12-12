package com.kdh.chart.datatypes;

import java.io.Serializable;

public class Chart implements Serializable {

    private String mChartName;
    private String mDescription;


    Chart(String chartName, String description) {
        this.mChartName = chartName;
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



    public void setChartName(String mChartName) {
        this.mChartName = mChartName;
    }

}
