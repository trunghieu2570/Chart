package com.kdh.chart.datatypes;

import java.io.Serializable;

public class ChartLocation implements Serializable {

    private String mLocation;
    private ChartTypeEnum mType;


    public ChartLocation(String location, ChartTypeEnum type) {
        this.mLocation = location;
        this.mType = type;
    }

    public ChartTypeEnum getType() {
        return mType;
    }

    public void setType(ChartTypeEnum type) {
        this.mType = type;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getLocation() {
        return mLocation;
    }
}
