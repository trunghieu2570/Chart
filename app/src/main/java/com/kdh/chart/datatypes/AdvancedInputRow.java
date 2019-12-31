package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.List;

public class AdvancedInputRow implements Serializable {
    private String mLabel;
    private int mColor;
    private List<String> mValues;

    public AdvancedInputRow(String label, int color, List<String> values) {
        this.mColor = color;
        this.mLabel = label;
        this.mValues = values;
    }

    public int getColor() {
        return mColor;
    }

    public List<String> getValues() {
        return mValues;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

}
