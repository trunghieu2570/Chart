package com.kdh.chart.datatypes;

import java.io.Serializable;

public class InputDataRow implements Serializable {
    private String mLabel;
    private int mColor;
    private Object mValue;

    public InputDataRow(String label, int color, Object value) {
        this.mColor = color;
        this.mLabel = label;
        this.mValue = value;
    }

    public int getColor() {
        return mColor;
    }

    public Object getValue() {
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

    public void setValue(Object value) {
        this.mValue = value;
    }
}
