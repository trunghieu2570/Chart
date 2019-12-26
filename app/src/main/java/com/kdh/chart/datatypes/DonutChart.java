package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class DonutChart extends Chart implements Serializable {

    private ArrayList<AdvancedInputRow> mData;
    private String objMeaning;
    private String valueName;
    private String valuesMeaning;
    private String timeName;

    public DonutChart(String chartName, String description, String objMeaning, String valueName, String valuesMeaning, String timeName) {
        super(chartName, description);
        this.objMeaning = objMeaning;
        this.valueName = valueName;
        this.valuesMeaning = valuesMeaning;
        this.timeName = timeName;
    }

    public ArrayList<AdvancedInputRow> getData() {
        return mData;
    }

    public void setData(ArrayList<AdvancedInputRow> mData) {
        this.mData = mData;
    }

    public String getObjMeaning() {
        return objMeaning;
    }

    public String getValueName() {
        return valueName;
    }
    public String getValuesMeaning() {
        return valuesMeaning;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setValuesMeaning(String valuesMeaning) {
        this.valuesMeaning = valuesMeaning;
    }

}
