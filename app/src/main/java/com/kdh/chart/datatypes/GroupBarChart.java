package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupBarChart extends Chart implements Serializable {
    private ArrayList<AdvancedInputRow> mData;

    public GroupBarChart(String chartName, String description) {
        super(chartName, description);
    }

    public ArrayList<AdvancedInputRow> getData() {
        return mData;
    }

    public void setData(ArrayList<AdvancedInputRow> mData) {
        this.mData = mData;
    }
}