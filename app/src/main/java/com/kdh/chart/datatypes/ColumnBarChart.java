package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class ColumnBarChart extends Chart implements Serializable {
    private ArrayList<AdvancedInputRow> mData;

    public ColumnBarChart(String chartName, String description) {
        super(chartName, description);
    }

    public ArrayList<AdvancedInputRow> getData() {
        return mData;
    }

    public void setData(ArrayList<AdvancedInputRow> mData) {
        this.mData = mData;
    }
}