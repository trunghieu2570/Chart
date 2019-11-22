package com.kdh.chart.charts;

import com.kdh.chart.datatypes.SimpleInputRow;

import java.util.List;

public interface ChartView {
    void updateData(List<SimpleInputRow> objects);
}
