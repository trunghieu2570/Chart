package com.kdh.chart.datatypes;

public class ChartTypeItem {

    private int imageResouce;
    private String nameChart;

    public ChartTypeItem(int imageResouce, String nameChart) {
        this.imageResouce = imageResouce;
        this.nameChart = nameChart;
    }

    public void setimageResouce(int imageResouce) {
        this.imageResouce = imageResouce;
    }

    public void setNameChart(String nameChart) {
        this.nameChart = nameChart;
    }

    public int getimageResouce() {
        return imageResouce;
    }

    public String getNameChart() {
        return nameChart;
    }


}
