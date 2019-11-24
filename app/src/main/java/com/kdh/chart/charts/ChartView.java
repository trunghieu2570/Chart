package com.kdh.chart.charts;

import java.util.List;

public interface ChartView<E> {
    void updateData(List<E> objects);
}
