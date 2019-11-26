package com.kdh.chart.datatypes;

import java.io.Serializable;

public class ChartLocation implements Serializable {

    private String mLocation;
    private ChartTypes mType;


    public ChartLocation(String location, ChartTypes type) {
        this.mLocation = location;
        this.mType = type;
    }

    public ChartTypes getType() {
        return mType;
    }

    public void setType(ChartTypes type) {
        this.mType = type;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getLocation() {
        return mLocation;
    }
}
