package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class PieChart extends Chart implements Serializable {

    private ArrayList<SimpleInputRow> mData;

    public PieChart(String chartName, String description, String modifiedTime) {
        super(chartName, description, modifiedTime);
    }

    public ArrayList<SimpleInputRow> getData() {
        return mData;
    }

    public void setData(ArrayList<SimpleInputRow> mData) {
        this.mData = mData;
    }
}
