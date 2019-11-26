package com.kdh.chart.datatypes;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    private String name;
    private String location;
    private String modifiedTime;
    private ArrayList<ChartLocation> charts;

    public Project(String name, String modifiedTime) {
        this.name = name;
        this.modifiedTime = modifiedTime;
        charts = new ArrayList<>();
    }

    public void setSaveLocation(String saveLocation) {
        this.location = saveLocation;
    }

    public String getSaveLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public ArrayList<ChartLocation> getCharts() {
        return charts;
    }

    public void setCharts(ArrayList<ChartLocation> charts) {
        this.charts = charts;
    }

    public void addChart(ChartLocation chart) {
        this.charts.add(chart);
    }
}
