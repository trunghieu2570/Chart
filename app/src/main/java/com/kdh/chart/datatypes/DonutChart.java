package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class DonutChart extends Chart implements Serializable {

    private ArrayList<AdvancedInputRow> mData;
    private String valuesMeaning;
    private String seriesMeaning;

    public DonutChart(String chartName, String description, String valuesMeaning, String seriesMeaning) {
        super(chartName, description);
        this.valuesMeaning = valuesMeaning;
        this.seriesMeaning = seriesMeaning;
    }

    public ArrayList<AdvancedInputRow> getData() {
        return mData;
    }

    public void setData(ArrayList<AdvancedInputRow> mData) {
        this.mData = mData;
    }

    public String getValuesMeaning() {
        return valuesMeaning;
    }

    public void setValuesMeaning(String valuesMeaning) {
        this.valuesMeaning = valuesMeaning;
    }

    public String getSeriesMeaning() {
        return seriesMeaning;
    }

    public void setSeriesMeaning(String seriesMeaning) {
        this.seriesMeaning = seriesMeaning;
    }
}
