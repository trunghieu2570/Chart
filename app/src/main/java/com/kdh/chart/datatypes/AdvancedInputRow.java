package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.List;

public class AdvancedInputRow implements Serializable {
    private String mLabel;
    private int mColor;
    private List<String> mValues;
    private String mDescription;

    public AdvancedInputRow(String label, int color, List<String> values, String description) {
        this.mColor = color;
        this.mLabel = label;
        this.mValues = values;
        this.mDescription = description;
    }

    public int getColor() {
        return mColor;
    }

    public List<String> getValues() {
        return mValues;
    }

    public void setValue(List<String> values) {
        this.mValues = values;
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



    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }
}
