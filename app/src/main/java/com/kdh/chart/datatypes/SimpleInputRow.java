package com.kdh.chart.datatypes;

import java.io.Serializable;

public class SimpleInputRow implements Serializable {
    private String mLabel;
    private int mColor;
    private String mValue;
    private String mDescription;

    public SimpleInputRow(String label, int color, String value, String description) {
        this.mColor = color;
        this.mLabel = label;
        this.mValue = value;
        this.mDescription = description;
    }

    public int getColor() {
        return mColor;
    }

    public String getValue() {
        return mValue;
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

    public void setValue(String value) {
        this.mValue = value;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }
}
